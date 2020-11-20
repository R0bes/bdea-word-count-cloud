package com.example.tagcloud.storage

import org.apache.commons.io.FileUtils
import org.hibernate.bytecode.BytecodeLogger.LOGGER
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@Component
class StorageManager @Autowired constructor (
        storageProperty: StorageProperty
) {

    val docSuffix = "txt"
    val cloudSuffix = "png"

    val basePath: Path = Paths.get(storageProperty.basePath).toAbsolutePath().normalize()
    val docsPath: Path = basePath.resolve(storageProperty.textDir)
    val cloudsPath: Path = basePath.resolve(storageProperty.imageDir)
    val wordCountPath: Path = basePath.resolve(storageProperty.wordCountDir)

    init {
        if(storageProperty.deleteOnRestart) {
            FileUtils.deleteDirectory(basePath.toFile())
            LOGGER.info("${basePath} deleted")
        }
        Files.createDirectories(basePath)
        Files.createDirectories(docsPath)
        Files.createDirectories(cloudsPath)
        Files.createDirectories(wordCountPath)
    }

    fun docPath(name: String): Path = docsPath.resolve("${name}.${docSuffix}")
    fun cloudPath(name: String): Path = cloudsPath.resolve("${name}.${cloudSuffix}")
    fun wordCountTmpPath(name: String): Path = wordCountPath.resolve("tmp-${name}")
    fun wordCountOutPath(name: String): Path = wordCountPath.resolve("out-${name}")
}