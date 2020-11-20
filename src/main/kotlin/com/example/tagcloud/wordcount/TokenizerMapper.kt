package com.example.tagcloud.wordcount

import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Mapper
import org.hibernate.bytecode.BytecodeLogger.LOGGER
import java.util.regex.Matcher
import java.util.regex.Pattern


class TokenizerMapper: Mapper<Any, Text, Text, IntWritable>() {

    private val one = IntWritable(1)
    private val word = Text()

    override fun map(key: Any, value: Text, context: Context) {
        val pattern: Pattern = Pattern.compile("(\\b[^\\s]+\\b)")
        val matcher: Matcher = pattern.matcher(value.toString())
        while (matcher.find()) {
            word.set(value.toString().substring(matcher.start(), matcher.end()).toLowerCase())
            context.write(word, one)
        }
    }
}