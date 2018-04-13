package com.app.wte.serviceImpl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

import com.app.wte.model.TestRunRequest;
import com.app.wte.service.Task;

@Service(value="fileGenerationTask")
public class FileGenerationTask implements Task {
	
	@Value("${tempLocation}")
	private String tempLocation;
	
	@Value("${templatePath}")
	private String templatePath;
	
	@Value("${destFile}")
	private String destFile;
	

	@Override
	public void execute(TestRunRequest testRunRequest) {
		FileInputStream excelFile;
		XSSFRow row;
		XSSFCell cell;
		XSSFCell cellVal;
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		try {
			excelFile = new FileInputStream(new File(tempLocation+ File.separator +testRunRequest.getFileName()));
		
			XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
			XSSFSheet sheet = workbook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();			
			int cells = sheet.getRow(0).getPhysicalNumberOfCells();
			String[][] xLvalue = new String[rows][cells];

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
							System.out.println("TestData key: "+xLvalue[r][c]+" val: "+xLvalue[r][c+1]);
							}
						}
						
					}
					if(!parameterMap.isEmpty()){
						templateProcess(parameterMap);						
					}
				}
				
			}

			workbook.close();
			if (excelFile != null) {
				excelFile.close();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void templateProcess(Map<String, Object> parameterMap) {
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
			file = new FileWriter (destFile,true);
			bw = new BufferedWriter(file); 
			pw = new PrintWriter(bw);
			cfg.setDirectoryForTemplateLoading(new File(templatePath));
			Template template = cfg.getTemplate("fields.txt");
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
		// System.out.println("Template: "+inboundText.toString());		
	}
	
}
