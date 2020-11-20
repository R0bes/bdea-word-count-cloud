package com.example.tagcloud.wordcount

import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Reducer
import org.hibernate.bytecode.BytecodeLogger.LOGGER


class WordCountReducer: Reducer<Text, IntWritable, Text, IntWritable>() {
    private val result = IntWritable()
    private val newKey = Text()

    override fun reduce(key: Text, values: Iterable<IntWritable>, context: Context) {
        val it = values.iterator()
        result.set(it.next().get() * it.next().get())
        val s = key.toString()
        newKey.set(s.substring(0, s.indexOf(",")))
        context.write(newKey, result)
    }
}