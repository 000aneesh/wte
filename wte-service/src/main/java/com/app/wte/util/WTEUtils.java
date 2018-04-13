package com.app.wte.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WTEUtils {

	//public static final Path TEMP_LOCATION = Paths.get(new File("").getAbsolutePath() + File.separator + "wte-temp" + File.separator + getDateTimeStamp());
	
	public static String getDateTimeStamp() {
		String pattern = "ddMMyyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date());
	}
	
	public static long getMilliSecTimeStamp() {
		return new Date().getTime();
	}
	
	public static Path geTempPath() {
		Path tempPath = Paths.get(new File("").getAbsolutePath() + File.separator + "wte-temp" + File.separator + getDateTimeStamp());
		return tempPath;
	}
}
