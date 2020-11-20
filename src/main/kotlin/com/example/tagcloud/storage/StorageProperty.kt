package com.example.tagcloud.storage

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component


@Component
@ConfigurationProperties("data.storage")
class StorageProperty {
    var basePath: String = "data"
    var textDir: String = "docs"
    var imageDir: String = "clouds"
    var wordCountDir: String = "wordcount"
    var deleteOnRestart: Boolean = true
}