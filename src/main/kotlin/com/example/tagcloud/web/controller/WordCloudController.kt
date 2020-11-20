package com.example.tagcloud.web.controller

import com.example.tagcloud.business.service.CloudService
import com.example.tagcloud.business.service.DocumentService
import com.example.tagcloud.business.service.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@Controller
class WordCloudController @Autowired constructor(
        private val cloudService: CloudService,
        private val documentService: DocumentService,
        private val storageService: StorageService
) {
    @ModelAttribute("allDocuments")
    fun populateDocuments() = documentService.findAll()

    @PostMapping("api/text/upload", consumes = ["multipart/form-data"])
    fun uploadTextFiles(@RequestParam(value = "documents") files: Array<MultipartFile>) : ResponseEntity<String> {
        documentService.newDocuments(files)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping("api/image/{name}")
    fun getImage(@PathVariable("name") name: String) : ResponseEntity<ByteArray?> {
        val imageBytes = storageService.getImageAsByteArray(name)
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes)
    }

    @RequestMapping("/home")
    fun allClouds() = "home.html"

    @RequestMapping("/cloud/{name}")
    fun fromDatabaseAsResEntity(@PathVariable("name") name: String): ResponseEntity<ByteArray?>
            = getImage(name)

    @RequestMapping("/cloud/corpus")
    fun result(): ResponseEntity<ByteArray?>
            = getImage("korpus")
}