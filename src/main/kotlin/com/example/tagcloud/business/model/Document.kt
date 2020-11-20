package com.example.tagcloud.business.model

import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*


@Entity(name="docs")
data class Document (

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="_id", nullable = false, unique = true)
        var id: Long? = null,

        @Column(name="filename", nullable = false, unique = true)
        var name: String? = null,

        @Column(name="doc_saved")
        var fileSaved: Boolean = false,

        @Column(name="cloud_saved")
        var cloudSaved: Boolean = false,

        @Column(name="corpus_updated")
        var corpusUpdated: Boolean = false
)