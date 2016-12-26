/**
 * Project Name:jettyTest
 * File Name:ReadProperties.java
 * Package Name:com.createproject
 * Date:2016年4月8日下午3:41:44
 * Copyright (c) 2016, ulewo.com All Rights Reserved.
 *
*/

package com.ulewo.easyjava.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
	private static Properties prop = new Properties();

	static {
		InputStream is = null;
		try {
			String classzName = new Object() {
				public String getClassName() {
					String clazzName = this.getClass().getName();
					return clazzName.substring(0, clazzName.lastIndexOf('$'));
				}
			}.getClassName();
			is = Class.forName(classzName).getResourceAsStream("/config.properties");
			prop.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getString(String key) {
		String value = prop.getProperty(key, "");
		return value;
	}
}
