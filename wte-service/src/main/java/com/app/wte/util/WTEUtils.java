package com.app.wte.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;

import com.app.wte.model.ExecutionContext;
import com.app.wte.model.TestRecord;
import com.app.wte.type.ExecutionStatusType;
import com.app.wte.type.ExecutionStepType;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

public class WTEUtils {

	@Value("${upload-path}")
	private static String uploadPath;

	@Value("${templatePath}")
	private static String templatePath;
	
	@Value("${ftpFilePath}")
	private static String ftpFilePath;
	
	@Value("${host}")
	private static String host;
	
	@Value("${port}")
	private static String port;
	
	@Value("${username}")
	private static String userName;
	
	@Value("${password}")
	private static String password;

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

	public static void jaxbObjectToXML(ExecutionContext executionContext,String folderName) {

        try {
        	File resultDir=new File(uploadPath+File.separator+executionContext.getResultFolderName()+File.separator+"Results");
        	resultDir.mkdirs();
        	
        	String FILE_NAME=uploadPath+"/"+executionContext.getResultFolderName()+"/Results/execution-status.xml";        	
            JAXBContext context = JAXBContext.newInstance(ExecutionContext.class);
            Marshaller m = context.createMarshaller();
            //for pretty-print XML in JAXB
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); 
            // Write to File
            m.marshal(executionContext, new File(FILE_NAME));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
	public static void readFromExcel(ExecutionContext executionContext) throws IOException{
		
		FileInputStream excelFile;
		XSSFRow row;
		XSSFCell cell;
		XSSFCell cellVal;
		Map<String, String> parameterMap = new LinkedHashMap<String, String>();	
		Map<String,TestRecord> testRecordMap=new LinkedHashMap<String,TestRecord>();
		
		//File testDataDir=new File(uploadPath+File.separator+executionContext.getResultFolderName()+File.separator+"TestData");
		//testDataDir.mkdirs();
		String inputFile=uploadPath+File.separator+executionContext.getResultFolderName()+File.separator+"TestData"
						+File.separator+executionContext.getResultFolderName()+".txt";
		String templateFile="fields.txt";

		excelFile = new FileInputStream(new File(uploadPath+File.separator+executionContext.getResultFolderName()
						+File.separator+"TestData"+File.separator+executionContext.getFileName()));
	
		XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
		XSSFSheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();			
		int cells = sheet.getRow(0).getPhysicalNumberOfCells();
		String[][] xLvalue = new String[rows][cells];
		int key=1;
		
		for (int r = 1; r < rows; r++) {
			
			row = sheet.getRow(r); // bring row
			if (row != null) {
				
				for (int c = 0; c < cells; c+=2) {
					cell = row.getCell(c);
					if (cell != null) {
						switch (cell.getCellType()) {
						case XSSFCell.CELL_TYPE_FORMULA:
							xLvalue[r][c] = cell.getCellFormula();
							break;
						case XSSFCell.CELL_TYPE_NUMERIC:
							xLvalue[r][c] = "" + (long)cell.getNumericCellValue();
							break;
						case XSSFCell.CELL_TYPE_STRING:
							xLvalue[r][c] = "" + cell.getStringCellValue();
							break;
						/*
						 * case XSSFCell.CELL_TYPE_BLANK: xLvalue[r][c] =
						 * "[BLANK]"; break;
						 */
						case XSSFCell.CELL_TYPE_ERROR:
							xLvalue[r][c] = "" + cell.getErrorCellValue();
							break;
						default:
						}
						cellVal=row.getCell(c+1);
						if (cellVal != null) {
							switch (cellVal.getCellType()) {
							case XSSFCell.CELL_TYPE_FORMULA:
								xLvalue[r][c+1] = cellVal.getCellFormula();
								break;
							case XSSFCell.CELL_TYPE_NUMERIC:
								xLvalue[r][c+1] = "" + (long)cellVal.getNumericCellValue();
								break;
							case XSSFCell.CELL_TYPE_STRING:
								xLvalue[r][c+1] = "" + cellVal.getStringCellValue();
								break;
							/*
							 * case XSSFCell.CELL_TYPE_BLANK: xLvalue[r][c] =
							 * "[BLANK]"; break;
							 */
							case XSSFCell.CELL_TYPE_ERROR:
								xLvalue[r][c+1] = "" + cellVal.getErrorCellValue();
								break;
							default:
							}
							
						parameterMap.put(xLvalue[r][c], xLvalue[r][c+1]);
						//key=xLvalue[r][1];
						System.out.println("TestData key: "+xLvalue[r][c]+" val: "+xLvalue[r][c+1]+" key: "+key);
						}
					}
					
				}
				if(!parameterMap.isEmpty()){	
					TestRecord testRecord=new TestRecord();
					testRecord.getInputTestData().putAll(parameterMap);
					testRecordMap.put(xLvalue[r][1],testRecord); 									
					templateProcess(parameterMap,inputFile,templateFile);
					key++;
				}
			}
			
		}
		executionContext.getExecutionResult().setTestRecordMap(testRecordMap);
		workbook.close();
		if (excelFile != null) {
			excelFile.close();
		}
	
	}
	public static void templateProcess(Map<String, String> parameterMap,String inputFile, String templateFile) {
		Writer file = null;
		BufferedWriter bw = null;
		PrintWriter pw = null;
		Configuration cfg = new Configuration();
		cfg.setIncompatibleImprovements(new Version(2, 3, 20));
	    //cfg.setDefaultEncoding("UTF-8");
	    //cfg.setLocale(Locale.US);
	    //cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		//Writer inboundText = new StringWriter();		
		
		try {
			file = new FileWriter (inputFile,true);
			bw = new BufferedWriter(file); 
			pw = new PrintWriter(bw);
			cfg.setDirectoryForTemplateLoading(new File(templatePath));
			Template template = cfg.getTemplate(templateFile);
			template.process(parameterMap, file);
			pw.println("");
		} catch (IOException | TemplateException e) {
			 System.out.println("Exception occurred-Template not found"+e);
			
		}finally{
			pw.close();
			 try {
				 bw.close();           
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void copyToServer(ExecutionContext executionContext) throws JSchException, SftpException{
		int portNumber= Integer.parseInt(port);
		Session session=null;
		Channel channel=null;
		ChannelSftp channelSftp=null;
		boolean copyToFtp=false;
		
	        JSch jsch = new JSch();
	        session = jsch.getSession( userName, host, portNumber );
	        session.setPassword(password);
	        java.util.Properties config = new java.util.Properties(); 
	        config.put("StrictHostKeyChecking", "no");
	        config.put("PreferredAuthentications","password");
	        session.setConfig(config);
	        session.connect();
	        channel = session.openChannel("sftp");
	        channel.connect();
	        System.out.println("sftp channel opened and connected.");
	        channelSftp = (ChannelSftp) channel;
	        File file = new File(uploadPath+File.separator+executionContext.getResultFolderName()+File.separator+"TestData"
	        				+File.separator+executionContext.getResultFolderName()+".txt");
	            if (file.isFile()){
	                String filename=file.getAbsolutePath();
	                channelSftp.put(filename, ftpFilePath+"/"+executionContext.getResultFolderName()+".txt", ChannelSftp.OVERWRITE);
	                System.out.println(filename + " transferred to " );
	                copyToFtp=true;
	            }
	    
			channelSftp.exit();
			channel.disconnect();
			session.disconnect();
		
	}
	
	public static void updateStatus(ExecutionContext executionContext,ExecutionStepType executionStepType,ExecutionStatusType executionStatusType){
		
		executionContext.getTaskSatusMap().put(executionStepType, executionStatusType);
	
	}
	public static ExecutionStatusType getStatus(ExecutionContext executionContext,ExecutionStepType executionStepType){
		
		return executionContext.getTaskSatusMap().get(executionStepType);
	
	}
	public static ExecutionStatusType getExecutionSatus(ExecutionContext executionContext,String executionStep){
		ExecutionStatusType executionStatusType = null;
		
		for(ExecutionStepType executionStepType:ExecutionStepType.values()){
			if(executionStepType.getExecutionStep().equalsIgnoreCase(executionStep)){
				executionStatusType=executionContext.getTaskSatusMap().get(executionStepType);
			}
		}
		return executionStatusType;
	}
	public static ExecutionStepType getExecutionStep(ExecutionContext executionContext,String executionStep){
		
		for(ExecutionStepType executionStepType:ExecutionStepType.values()){
			if(executionStepType.getExecutionStep().equalsIgnoreCase(executionStep)){
				return executionStepType;
			}
		}
		return null;
	}
}
