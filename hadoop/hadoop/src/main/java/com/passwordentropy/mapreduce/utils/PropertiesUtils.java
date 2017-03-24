package com.passwordentropy.mapreduce.utils;

import java.io.InputStream;
import java.util.Properties;


public class PropertiesUtils {
	Properties prop = new Properties();

	public PropertiesUtils() {
		loadProperties();
	}

	public void loadProperties() {
		try {
			String propFileName = "config.properties";
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(propFileName);
			if (inputStream != null) {
				prop.load(inputStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public String getHadoopFSUrl() {
		if (prop == null)
			loadProperties();
		return prop.getProperty("fs.defaultFS");
	}

	public String getHadoopDefaultFileName() {
		if (prop == null)
			loadProperties();
		return prop.getProperty("fs.defaultOutputFileName");
	}

}
