package com.example.tagcloud

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TagcloudApplication

fun main(args: Array<String>) {
    runApplication<TagcloudApplication>(*args)
}
