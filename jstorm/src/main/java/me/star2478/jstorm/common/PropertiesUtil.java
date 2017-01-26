package me.star2478.jstorm.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	private static Properties properties;

	private PropertiesUtil() {
	}

	public static String getProperty(String key) {
		if (properties == null) {
			synchronized (PropertiesUtil.class) {
				if (properties == null) {
					try {
						properties = new Properties();
						InputStream is = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties");
						properties.load(is);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return properties.getProperty(key);
	}

	public static void main(String[] args) {
		try {
			String name = getProperty("name");
			System.out.println("name is:" + name);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("---file not exist---");
		}
	}
}
