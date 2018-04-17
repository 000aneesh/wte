package com.app.wte.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Value;

import com.app.wte.constants.WTEConstants;
import com.app.wte.model.TestResult;

public class WTEUtils {

	@Value("${upload-path}")
	private String uploadPath;

	public static final String BASE_PATH = new File("").getAbsolutePath() + File.separator;

	public static String getUniqueTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String strDate = sdfDate.format(new Date());
		return strDate;

		// return new Date().getTime();
	}

	// public static String getUploadPath() throws IOException {
	// String tempPath = getExternalProperty("upload-path") + File.separator;
	// return tempPath;
	// }

//	public static String getExternalProperty(String propertyKey) throws IOException {
//		Properties prop = new Properties();
//		FileInputStream inputStream = new FileInputStream(
//				BASE_PATH + WTEConstants.EXT_PROPERTY_FOLDER + File.separator + WTEConstants.EXT_PROPERTY_FILE);
//		prop.load(inputStream);
//		String propertyValue = prop.getProperty(propertyKey);
//		return propertyValue;
//	}

	public static void jaxbObjectToXML(TestResult testResult, String folderName) {

		try {
			String fileName = testResult.getResultFolderName() + File.separator + "execution-status.xml";

			// String
			// FILE_NAME=testResult.getResultFolderName()+"\\Results\\execution-status.xml";
			JAXBContext context = JAXBContext.newInstance(TestResult.class);
			Marshaller m = context.createMarshaller();
			// for pretty-print XML in JAXB
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			// Write to File
			m.marshal(testResult, new File(fileName));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
