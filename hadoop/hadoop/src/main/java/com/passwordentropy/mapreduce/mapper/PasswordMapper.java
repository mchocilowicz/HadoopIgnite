package com.passwordentropy.mapreduce.mapper;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.passwordentropy.mapreduce.utils.PasswordEntropyUtils;



public class PasswordMapper extends Mapper<LongWritable, Text, LongWritable, Text> {
	PasswordEntropyUtils passwordEntropyUtils = new PasswordEntropyUtils();

	private Text password = new Text();

	private LongWritable passwordStrenth = new LongWritable();


	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, LongWritable, Text>.Context context)
			throws IOException, InterruptedException, NullPointerException {

 		String passwordFromFile = value.toString();

		password.set(passwordFromFile);
		try {

			passwordEntropyUtils.calculateStrength(passwordFromFile);

			passwordStrenth.set(passwordEntropyUtils.getScore());

			context.write(passwordStrenth, password);
		} catch (NumberFormatException e) {
		}
	}
}
