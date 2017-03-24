package com.passwordentropy.mapreduce.service;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.springframework.stereotype.Service;

import com.passwordentropy.mapreduce.mapper.PasswordMapper;
import com.passwordentropy.mapreduce.utils.PropertiesUtils;


@Service
public class MapReduceToolRunnerService extends Configured implements Tool {

	PropertiesUtils propsFile = new PropertiesUtils();

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = this.getConf();
		System.out.println("Props " + propsFile);
		conf.set("fs.defaultFS", propsFile.getHadoopFSUrl());
		System.out.println("In run job..,...");
		Job job = new Job(conf, "Password Entropy Job");
		job.setJarByClass(MapReduceToolRunnerService.class);

		job.setMapperClass(PasswordMapper.class);

		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);
		job.setSortComparatorClass(LongWritable.DecreasingComparator.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		job.setInputFormatClass(TextInputFormat.class);

		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setOutputFormatClass(TextOutputFormat.class);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public int submitJob(String inputDir, String outputDir) throws Exception {
		System.out.println("In submit job,...");
		return ToolRunner.run(new Configuration(), new MapReduceToolRunnerService(),
				new String[] { inputDir, outputDir });
	}
}
