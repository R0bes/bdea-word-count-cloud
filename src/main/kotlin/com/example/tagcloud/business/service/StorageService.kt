package com.example.tagcloud.business.service

import com.example.tagcloud.business.repository.DocumentRepository
import com.example.tagcloud.storage.StorageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hibernate.bytecode.BytecodeLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.file.Files
import javax.imageio.ImageIO

@Component
class StorageService@Autowired constructor(
        private val documentRepository: DocumentRepository,
        private val storageManager: StorageManager
) {

    suspend fun writeTextFileToDir(name: String, inpuStream: InputStream) : Boolean =
            withContext(Dispatchers.IO) {
                var error = false;
                try {
                    Files.copy(inpuStream, storageManager.docPath(name))
                } catch (ex: Exception) {
                    error = true
                    BytecodeLogger.LOGGER.info("file save failed: ${ex.message}")
                }
                return@withContext !error
            }


    fun getImageAsByteArray(name: String): ByteArray {
        val doc = documentRepository.findByName(name)
        val bufferedImage: BufferedImage = ImageIO.read(storageManager.cloudPath(doc.name!!).toFile())
        val oStream = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "png", oStream)
        return oStream.toByteArray()
    }
}