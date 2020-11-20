package com.example.tagcloud.business.service

import com.example.tagcloud.business.model.Document
import com.example.tagcloud.business.repository.DocumentRepository
import com.example.tagcloud.storage.StorageManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import org.hibernate.bytecode.BytecodeLogger
import org.hibernate.bytecode.BytecodeLogger.LOGGER
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.file.Files


@Service
class DocumentService @Autowired constructor(
        private val documentRepository: DocumentRepository,
        private val cloudService: CloudService,
        private val storageManager: StorageManager,
        private val mapReduceService: MapReduceService,
        private val storageService: StorageService
) {
    fun findAll() = documentRepository.findAll()

    fun newDocuments(documents: Array<MultipartFile>) {
        documents.forEach {
            multipartFile ->
            if (multipartFile.contentType == "text/plain") {

                val document = Document()
                document.name = FilenameUtils.removeExtension(multipartFile.originalFilename)
                val inpuStream = multipartFile.inputStream
                documentRepository.save(document)

                // start lambda
                GlobalScope.async { batchLane(document, inpuStream) } // long
                fastLane(document, inpuStream)  //short

            } else {
                LOGGER.info("wrong document type: ${multipartFile.contentType}")
            }
        }
    }

    private fun fastLane(document: Document, inpuStream: InputStream) {

        GlobalScope.launch {

            val fileSaved = storageService.writeTextFileToDir(document.name!!, inpuStream)
            if(!fileSaved) {
                BytecodeLogger.LOGGER.info("file save failed")
                documentRepository.delete(document)
                return@launch
            }

            document.fileSaved = true
            documentRepository.save(document)

            val outputStream = FileOutputStream(storageManager.cloudPath(document.name!!).toFile())
            val cloudSaved = cloudService.processCloud(storageManager.docPath(document.name!!), outputStream)
            outputStream.flush()

            if (!cloudSaved) {
                Files.delete(storageManager.docPath(document.name!!))
                documentRepository.delete(document)
                LOGGER.info("cloud save failed")
                return@launch
            }
            document.cloudSaved = true
            documentRepository.save(document)
            LOGGER.info("document saved")
        }
    }


    suspend private fun batchLane(document: Document, inpuStream: InputStream) {
        val result = mapReduceService.runMapReduce(document.name!!)
        val outputStream = FileOutputStream(storageManager.cloudPath("corpus").toFile())
        val cloudSaved = cloudService.processCloud(storageManager.wordCountOutPath(document.name!!), outputStream)
        document.corpusUpdated = true
        documentRepository.save(document)
    }
}