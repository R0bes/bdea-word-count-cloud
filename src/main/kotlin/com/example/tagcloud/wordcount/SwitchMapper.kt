package com.example.tagcloud.wordcount

import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Mapper


class SwitchMapper : Mapper<Text, IntWritable, IntWritable, Text>() {
    override fun map(word: Text, count: IntWritable, context: Context) {
        context.write(count, word)
    }
}