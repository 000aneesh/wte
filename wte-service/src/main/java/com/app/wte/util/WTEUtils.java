package com.app.wte.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.app.wte.constants.WTEConstants;

public class WTEUtils {

	public static final String BASE_PATH = new File("").getAbsolutePath() + File.separator;

	public static String getDateTimeStamp() {
		String pattern = "ddMMyyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date());
	}

	public static long getUniqueTimeStamp() {
		return new Date().getTime();
	}

	public static String getUploadPath() {
		String tempPath = BASE_PATH + WTEConstants.UPLOAD_FOLDER + File.separator;
		return tempPath;
	}

	public static String getExternalProperty(String propertyKey) throws IOException {
		Properties prop = new Properties();
		FileInputStream inputStream = new FileInputStream(BASE_PATH + WTEConstants.EXT_PROPERTY_FOLDER + File.separator + WTEConstants.EXT_PROPERTY_FILE);
		prop.load(inputStream);
		String propertyValue = prop.getProperty(propertyKey);
		return propertyValue;
	}
}
