package com.example.tagcloud.wordcount

import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.WritableComparable
import org.apache.hadoop.io.WritableComparator


class DescendingComperator : WritableComparator(IntWritable::class.java, true) {
    override fun compare(a: WritableComparable<*>?, b: WritableComparable<*>?): Int {
        return super.compare(a, b) * -1
    }
}