package com.example.tagcloud.business.repository

import com.example.tagcloud.business.model.Document
import org.springframework.data.repository.CrudRepository

interface DocumentRepository : CrudRepository<Document, Long> {
    override fun findAll(): List<Document>
    override fun <D : Document> save(name: D): D
    override fun delete(document: Document)
    fun findByName(name: String): Document
}