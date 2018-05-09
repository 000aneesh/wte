package com.app.wte.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.app.wte.model.ExecutionContext;
import com.app.wte.model.ProcessValidationResult;
import com.app.wte.model.TestRecord;
import com.app.wte.model.TestResult;
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
	
	private static final Logger logger = LoggerFactory.getLogger(WTEUtils.class);
	
	
	public static String getUniqueTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String strDate = sdfDate.format(new Date());
		return strDate;
	}
	
	/*public void createResultsFolder(){
		String resultFolderName="Run-"+getUniqueTimeStamp();		
		File resultDir=new File(uploadPath+File.separator+resultFolderName);
		resultDir.mkdirs();
		
	}*/
	public static void jaxbObjectToXML(ExecutionContext executionContext, String uploadPath,String folderName) {

        try {
        	File resultDir=new File(uploadPath+File.separator+executionContext.getTestCase()+File.separator+"Results");
        	resultDir.mkdirs();
        	
        	String FILE_NAME=uploadPath+"/"+executionContext.getTestCase()+"/Results/execution-status.xml";        	
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
	
	public static ExecutionContext jaxbXMLToObject(String testCase, String uploadPath) {
		//fileName="D:/files/Run-20180417170132814/Results/execution-status.xml";  
		ExecutionContext executionContext = null;
		try {
			File file = new File(uploadPath+"/"+testCase+"/Results/execution-status.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ExecutionContext.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			executionContext = (ExecutionContext) jaxbUnmarshaller.unmarshal(file);
			//executionContext.getTestRecordList().forEach(testRecord -> System.out.println(testRecord.getTestCaseDescription()));
			//System.out.println(executionContext.getFileName());

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return executionContext;
	}
	public static void readFromExcel(ExecutionContext executionContext,String uploadPath, String templatePath) throws IOException{
		
		FileInputStream excelFile;
		XSSFRow row;
		XSSFCell cell;
		XSSFCell cellVal;
		XSSFCell cellExpVal;
		Map<String, String> inputTestData = new LinkedHashMap<String, String>();
		Map<String,String> expectedTestData=new LinkedHashMap<String,String>();
		List<TestRecord> testRecordList = new ArrayList<TestRecord>();
		
		File testDataDir=new File(uploadPath+File.separator+executionContext.getResultFolderName());
		testDataDir.renameTo(new File(uploadPath+File.separator+executionContext.getTestCase()));
		
		String inputFile=uploadPath+File.separator+executionContext.getTestCase()+File.separator+"TestData"
				+File.separator+executionContext.getConfigDataMap().get("templatePattern")+executionContext.getResultFolderName()+".txt";
		//String templateFile="fields.txt";
		String templateFile=executionContext.getTemplateKey()+".txt";
		
		File headerFile = new File(templatePath+"/"+executionContext.getTemplateKey()+"_header.txt");
		
		if(headerFile.exists()){
			File file = new File(inputFile);
			 Files.copy(headerFile.toPath(), file.toPath());
		}

		excelFile = new FileInputStream(new File(uploadPath+File.separator+executionContext.getTestCase()
						+File.separator+"TestData"+File.separator+executionContext.getFileName()));
	
		XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
		XSSFSheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();			
		int cells = sheet.getRow(0).getPhysicalNumberOfCells();
		String[][] xLvalue = new String[rows][cells];
		DataFormatter dataFormatter = new DataFormatter();
		int key=1;
		
		for (int r = 1; r < rows; r++) {
			
			row = sheet.getRow(r); // bring row
			if (row != null) {
				
				for (int c = 0; c < cells; c+=3) {
					cell = row.getCell(c);
					if (cell != null) {
						xLvalue[r][c] = dataFormatter.formatCellValue(cell);
						
						cellVal=row.getCell(c+1);
						if (cellVal != null) {
							xLvalue[r][c+1] = dataFormatter.formatCellValue(cellVal);
							
							inputTestData.put(xLvalue[r][c], xLvalue[r][c+1]);
							System.out.println("TestData key: "+xLvalue[r][c]+" val: "+xLvalue[r][c+1]+" key: "+key);
						}
						cellExpVal=row.getCell(c+2);
						if (cellExpVal != null) {
							xLvalue[r][c+2] = dataFormatter.formatCellValue(cellExpVal);
							
							expectedTestData.put(xLvalue[r][c], xLvalue[r][c+2]);
							System.out.println("TestData key: "+xLvalue[r][c]+" val: "+xLvalue[r][c+2]+" key: "+key);
						}
					}
					
				}
				
				if(!inputTestData.isEmpty()){	
					TestRecord testRecord=new TestRecord();
					testRecord.setTestCaseNumber(key);
					testRecord.setTestCaseDescription(row.getCell(cells-1)!=null?dataFormatter.formatCellValue(row.getCell(cells-1)):"");
					testRecord.getInputTestData().putAll(inputTestData);
					testRecord.getExpectedTestData().putAll(expectedTestData);
					testRecordList.add(testRecord);
					processTemplate(inputTestData,inputFile,templateFile,templatePath,true);
					key++;
				}
			}
			
		}
		executionContext.setTestRecordList(testRecordList);
		workbook.close();
		if (excelFile != null) {
			excelFile.close();
		}
	
	}
	public static void processTemplate(Map<String, String> parameterMap,String inputFile, String templateFile, String templatePath,boolean append) {
		Writer file = null;
		BufferedWriter bw = null;
		PrintWriter pw = null;
		Configuration cfg = new Configuration();
		cfg.setIncompatibleImprovements(new Version(2, 3, 20));
	    //cfg.setDefaultEncoding("UTF-8");
	    //cfg.setLocale(Locale.US);
	    //cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		
		try {
			file = new FileWriter (inputFile,append);
			cfg.setDirectoryForTemplateLoading(new File(templatePath));
			Template template = cfg.getTemplate(templateFile);
			template.process(parameterMap, file);
			if(append){
				bw = new BufferedWriter(file); 
				//file.write("\r\n");
				//bw.newLine();
				pw = new PrintWriter(bw);
				pw.println("");
			}
			
		} catch (IOException | TemplateException e) {
			 System.out.println("Exception occurred-Template not found"+e);
			
		}finally{
			if(pw!=null){
				pw.close();
			}
			 try {
				 if(bw!=null){
					 bw.close();  
				 }
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void copyToServer(ExecutionContext executionContext, String uploadPath, String ftpFilePath, 
			String host, String port, String userName, String password) throws JSchException, SftpException{
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
	        File file = new File(uploadPath+File.separator+executionContext.getTestCase()+File.separator+"TestData"
	        				+File.separator+executionContext.getConfigDataMap().get("templatePattern")+executionContext.getResultFolderName()+".txt");
	            if (file.isFile()){
	                String filename=file.getAbsolutePath();
	                channelSftp.put(filename, ftpFilePath+"/"+executionContext.getConfigDataMap().get("templatePattern")+executionContext.getResultFolderName()+".txt", ChannelSftp.OVERWRITE);
	                System.out.println(filename + " transferred to " );
	                copyToFtp=true;
	            }
	    
			channelSftp.exit();
			channel.disconnect();
			session.disconnect();
		
	}
	
	public static ExecutionStepType processValidationResult(Map<String, Object> result, ExecutionContext executionContext, String uploadPath){
		
			  ProcessValidationResult processValidationResult=new ProcessValidationResult();
			  
			  processValidationResult.setFileName(result.getOrDefault("FILE_NAME","").toString());
			  processValidationResult.setEfid(result.getOrDefault("EFID","").toString());
			  processValidationResult.setClientName(result.getOrDefault("CLIENT_NAME","").toString());
			  processValidationResult.setSourcePath(result.getOrDefault("SOURCE_PATH","").toString());
			  processValidationResult.setStageName(result.getOrDefault("STAGE_NAME","").toString());
			  processValidationResult.setStatus(result.getOrDefault("STATUS","").toString());
			  processValidationResult.setTotalRecordsCount((int)(result.getOrDefault("TOTAL_RECORDS_COUNT",0)));
			  processValidationResult.setValidRecordsCount((int)(result.getOrDefault("VALID_RECORDS_COUNT",0)));
			  processValidationResult.setInvalidRecordsCount((int)(result.getOrDefault("INVALID_RECORDS_COUNT",0)));
			  processValidationResult.setFileArrivalTimestamp(result.get("EDGE_NODE_FILE_ARRIVAL_TIMESTAMP")!=null?
					  new Date(new Timestamp((long) result.get("EDGE_NODE_FILE_ARRIVAL_TIMESTAMP")).getTime()):null);
			  processValidationResult.setRuleVersion((double)(result.getOrDefault("RULE_VERSION",0.0)));
			  processValidationResult.setIsCompressed(result.getOrDefault("IS_COMPRESSED","").toString());
			  processValidationResult.setProcessedTimestamp(result.get("PROCESSED_TIMESTAMP")!=null?
					  new Date(new Timestamp((long) result.get("PROCESSED_TIMESTAMP")).getTime()):null);
			  processValidationResult.setProcessedUserId(result.getOrDefault("PROCESSED_USER_ID","").toString());
			  
			  ExecutionStepType executionStepType = WTEUtils.getExecutionStep(result.getOrDefault("STAGE_NAME","").toString());
			  
			  executionContext.getTaskSatusDetailsMap().put(executionStepType,processValidationResult);	
			  
			  if(ExecutionStatusType.COMPLETED.toString().equalsIgnoreCase(result.getOrDefault("STATUS","").toString())){
				  executionContext.getTaskSatusMap().put(executionStepType,ExecutionStatusType.COMPLETED);
			  }else if(ExecutionStatusType.ERROR.toString().equalsIgnoreCase(result.getOrDefault("STATUS","").toString())){
				  executionContext.getTaskSatusMap().put(executionStepType,ExecutionStatusType.ERROR);
			  }else if(ExecutionStatusType.IN_PROGRESS.toString().equalsIgnoreCase(result.getOrDefault("STATUS","").toString())){
				  executionContext.getTaskSatusMap().put(executionStepType,ExecutionStatusType.IN_PROGRESS);
			  }
			  
			  WTEUtils.jaxbObjectToXML(executionContext,uploadPath,"");
			return executionStepType;		  
	}
	
	public static void mockProcessValidationResult(ExecutionContext executionContext,String inputFile, String uploadPath){
		ProcessValidationResult processValidationResult1=new ProcessValidationResult();
		  processValidationResult1.setStageName(ExecutionStepType.ProcessValidationEdgeToRaw.getExecutionStep());
		  processValidationResult1.setFileName(inputFile);
		  processValidationResult1.setStatus("COMPLETED");
		  processValidationResult1.setTotalRecordsCount(10);
		  processValidationResult1.setValidRecordsCount(8);
		  processValidationResult1.setInvalidRecordsCount(2);
		  long time=1521495435000l;
		  processValidationResult1.setProcessedTimestamp(new Date(new Timestamp(time).getTime()));
		  executionContext.getTaskSatusDetailsMap().put(ExecutionStepType.ProcessValidationEdgeToRaw,processValidationResult1);				  
		  executionContext.getTaskSatusMap().put(ExecutionStepType.ProcessValidationEdgeToRaw,ExecutionStatusType.COMPLETED);
		  
		  ProcessValidationResult processValidationResult2=new ProcessValidationResult();
		  processValidationResult2.setStageName(ExecutionStepType.ProcessValidationRawToRA.getExecutionStep());
		  processValidationResult2.setFileName(inputFile);
		  processValidationResult2.setStatus("COMPLETED");
		  processValidationResult2.setTotalRecordsCount(8);
		  processValidationResult2.setValidRecordsCount(8);
		  processValidationResult2.setInvalidRecordsCount(0);
		  time=1521495435000l;
		  processValidationResult2.setProcessedTimestamp(new Date(new Timestamp(time).getTime()));
		  executionContext.getTaskSatusDetailsMap().put(ExecutionStepType.ProcessValidationRawToRA,processValidationResult2);				  
		  executionContext.getTaskSatusMap().put(ExecutionStepType.ProcessValidationRawToRA,ExecutionStatusType.COMPLETED);
		  
		  ProcessValidationResult processValidationResult3=new ProcessValidationResult();
		  processValidationResult3.setStageName(ExecutionStepType.ProcessValidationRAToRaw.getExecutionStep());
		  processValidationResult3.setFileName(inputFile);
		  processValidationResult3.setStatus("COMPLETED");
		  processValidationResult3.setTotalRecordsCount(8);
		  processValidationResult3.setValidRecordsCount(8);
		  processValidationResult3.setInvalidRecordsCount(0);
		  time=1521495435000l;
		  processValidationResult3.setProcessedTimestamp(new Date(new Timestamp(time).getTime()));
		  executionContext.getTaskSatusDetailsMap().put(ExecutionStepType.ProcessValidationRAToRaw,processValidationResult3);				  
		  executionContext.getTaskSatusMap().put(ExecutionStepType.ProcessValidationRAToRaw,ExecutionStatusType.COMPLETED);
		  
		  ProcessValidationResult processValidationResult4=new ProcessValidationResult();
		  processValidationResult4.setStageName(ExecutionStepType.ProcessValidationRawToR.getExecutionStep());
		  processValidationResult4.setFileName(inputFile);
		  processValidationResult4.setStatus("COMPLETED");
		  processValidationResult4.setTotalRecordsCount(8);
		  processValidationResult4.setValidRecordsCount(8);
		  processValidationResult4.setInvalidRecordsCount(0);
		  time=1521495435000l;
		  processValidationResult4.setProcessedTimestamp(new Date(new Timestamp(time).getTime()));
		  executionContext.getTaskSatusDetailsMap().put(ExecutionStepType.ProcessValidationRawToR,processValidationResult4);				  
		  executionContext.getTaskSatusMap().put(ExecutionStepType.ProcessValidationRawToR,ExecutionStatusType.COMPLETED);
		  WTEUtils.jaxbObjectToXML(executionContext,uploadPath,"");
	}
	
	public static void mockVerficationResult(ExecutionContext executionContext){
		executionContext.getTestRecordList().get(0).setStatus("Success");
		executionContext.getTestRecordList().get(1).setStatus("Success");
		executionContext.getTestRecordList().get(2).setStatus("Failure");
		executionContext.getTestRecordList().get(3).setStatus("Success");
		executionContext.getTestRecordList().get(4).setStatus("Success");
		executionContext.getTestRecordList().get(5).setStatus("Failure");
		TestResult testResult=new TestResult();
		Map<String,String> testStatus=new LinkedHashMap<String,String>();
		testStatus.put("id", "Success");
		testStatus.put("email", "Success");
		testStatus.put("finding", "Failure");
		testStatus.put("comment", "Success");
		testResult.getTestStatus().putAll(testStatus);
		executionContext.getTestRecordList().get(0).setTestresult(testResult);
		TestResult testResult1=new TestResult();
		Map<String,String> testStatus1=new LinkedHashMap<String,String>();
		testStatus1.put("id", "Success");
		testStatus1.put("email", "Success");
		testStatus1.put("finding", "Success");
		testStatus1.put("comment", "Success");
		testResult1.getTestStatus().putAll(testStatus1);
		executionContext.getTestRecordList().get(1).setTestresult(testResult1);
	}
	
	public static Map<String, String> readFromExcel(String path, String fileName) throws IOException{
		
		FileInputStream excelFile;
		XSSFRow row;
		XSSFCell cell;
		XSSFCell cellVal;
		XSSFCell cellExpVal;
		Map<String, String> inputTestData = new LinkedHashMap<String, String>();
		
		logger.info("Excel file to be read--->"+path+fileName);
		excelFile = new FileInputStream(new File(path+fileName));
	
		XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
		XSSFSheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();			
		int cells = sheet.getRow(0).getPhysicalNumberOfCells();
		String[][] xLvalue = new String[rows][cells];
		DataFormatter dataFormatter = new DataFormatter();
		int key=1;
		
		for (int r = 1; r < rows; r++) {
			
			row = sheet.getRow(r); // bring row
			if (row != null) {
				
				for (int c = 0; c < cells; c+=2) {
					cell = row.getCell(c);
					if (cell != null) {
						xLvalue[r][c] = dataFormatter.formatCellValue(cell);
						
						cellVal=row.getCell(c+1);
						if (cellVal != null) {
							xLvalue[r][c+1] = dataFormatter.formatCellValue(cellVal);
							
							inputTestData.put(xLvalue[r][c], xLvalue[r][c+1]);
							System.out.println("TestData key: "+xLvalue[r][c]+" val: "+xLvalue[r][c+1]+" key: "+key);
						}
						
					}
					
				}
				
				
			}
			
		}
		
		workbook.close();
		if (excelFile != null) {
			excelFile.close();
		}
		return inputTestData;
	
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
	public static ExecutionStepType getExecutionStep(String executionStep){
		
		for(ExecutionStepType executionStepType:ExecutionStepType.values()){
			if(executionStepType.getExecutionStep().equalsIgnoreCase(executionStep)){
				return executionStepType;
			}
		}
		return null;
	}
}
