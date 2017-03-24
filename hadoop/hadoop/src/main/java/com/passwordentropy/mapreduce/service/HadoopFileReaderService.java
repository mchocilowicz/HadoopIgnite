package com.passwordentropy.mapreduce.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.stereotype.Service;

import com.passwordentropy.mapreduce.dto.PasswordStrengthDto;
import com.passwordentropy.mapreduce.utils.PropertiesUtils;

@Service
public class HadoopFileReaderService {
	PropertiesUtils propsFile = new PropertiesUtils();

	public List<PasswordStrengthDto> readFromHDFS(String directory) throws Exception {
		List<PasswordStrengthDto> _RESULTS = new ArrayList<PasswordStrengthDto>();

		String _LINE_SEPARATOR="\t";
		
		Configuration configuration = new Configuration();

		FileSystem fs = FileSystem.get(new URI(propsFile.getHadoopFSUrl()), configuration);

		Path filePath = new Path(new StringBuilder(directory).append("/").append(propsFile.getHadoopDefaultFileName()).toString());

		FSDataInputStream fsDataInputStream = fs.open(filePath);

		BufferedReader br = new BufferedReader(new InputStreamReader(fsDataInputStream));
		String line;
		String splitLineByTabs[];
		while ((line = br.readLine()) != null) {
			line = br.readLine();
			splitLineByTabs = line.split(_LINE_SEPARATOR);
			_RESULTS.add(new PasswordStrengthDto(splitLineByTabs[1], Long.parseLong(splitLineByTabs[0])));
		}
		return _RESULTS;
	}
}
