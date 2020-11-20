package com.example.tagcloud.business.service

import com.example.tagcloud.storage.StorageManager
import com.example.tagcloud.wordcount.MapReduce
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Paths


@Service
class MapReduceService  @Autowired constructor(
        private val mapReduce: MapReduce,
        private val storageManager: StorageManager
) {
    fun runMapReduce(name: String) {
        GlobalScope.launch {
            mapReduce.go(
                    storageManager.docsPath.toString(),
                    storageManager.wordCountTmpPath(name).toString(),
                    storageManager.wordCountOutPath(name).toString())
        }
    }
}