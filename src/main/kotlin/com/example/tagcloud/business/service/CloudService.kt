package com.example.tagcloud.business.service

import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.WordCloud
import com.kennycason.kumo.bg.CircleBackground
import com.kennycason.kumo.font.scale.SqrtFontScalar
import com.kennycason.kumo.nlp.FrequencyAnalyzer
import com.kennycason.kumo.palette.ColorPalette
import org.hibernate.bytecode.BytecodeLogger
import org.springframework.stereotype.Service
import java.awt.Color
import java.awt.Dimension
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path


@Service
class CloudService {

    fun processCloud(inputPath: Path, outputStream: OutputStream): Boolean
    {
        try {
            val wordFrequencies = FrequencyAnalyzer().load(inputPath.toString())
            val wordCloud = WordCloud(Dimension(600, 600), CollisionMode.PIXEL_PERFECT)
            wordCloud.setPadding(2)
            wordCloud.setBackground(CircleBackground(300))
            wordCloud.setColorPalette(ColorPalette(Color(0x4055F1), Color(0x408DF1), Color(0x40AAF1), Color(0x40C5F1), Color(0x40D3F1), Color(0xFFFFFF)))
            wordCloud.setFontScalar(SqrtFontScalar(10, 40))
            wordCloud.build(wordFrequencies)
            wordCloud.writeToStreamAsPNG(outputStream)
            return true
        } catch (ex: Exception) {
            return false
        }
    }
}