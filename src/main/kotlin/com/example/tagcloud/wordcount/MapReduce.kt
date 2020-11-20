package com.example.tagcloud.wordcount

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat
import org.apache.hadoop.mapreduce.lib.reduce.IntSumReducer
import org.hibernate.bytecode.BytecodeLogger.LOGGER
import org.springframework.stereotype.Controller
import java.lang.System.currentTimeMillis


@Controller
class MapReduce {

    fun go(inputPath: String, tempPath: String, outputPath: String) {

        val iPath = Path(inputPath)
        val tPath = Path(tempPath)
        val oPath = Path(outputPath)

        LOGGER.info("Start WordCount")
        LOGGER.info("Input path: ${inputPath}")
        LOGGER.info("Intermediate path: ${tempPath}")
        LOGGER.info("Out path: ${outputPath}")

        val conf = Configuration()
        var job = Job.getInstance(conf, "word-count-1")
        job.setJarByClass(MapReduce::class.java)
        job.mapperClass = TokenizerMapper::class.java // words to tokens
        job.combinerClass = IntSumReducer::class.java // reduce on node
        job.reducerClass = IntSumReducer::class.java // reduce all together
        job.numReduceTasks = 4
        job.outputKeyClass = Text::class.java
        job.outputValueClass = IntWritable::class.java

        job.outputFormatClass = SequenceFileOutputFormat::class.java

        FileInputFormat.addInputPath(job, iPath)
        FileOutputFormat.setOutputPath(job, tPath)

        job.waitForCompletion(true)

        job = Job.getInstance(conf, "word-count-2")
        job.setJarByClass(MapReduce::class.java)
        job.mapperClass = SwitchMapper::class.java
        job.reducerClass = Reducer::class.java
        job.numReduceTasks = 1
        job.outputKeyClass = IntWritable::class.java
        job.outputValueClass = Text::class.java

        job.setSortComparatorClass(DescendingComperator::class.java)
        job.inputFormatClass = SequenceFileInputFormat::class.java

        FileInputFormat.addInputPath(job, tPath)
        FileOutputFormat.setOutputPath(job, oPath)

        job.waitForCompletion(true)



    }
}