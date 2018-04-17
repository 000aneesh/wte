package com.app.wte.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

@Component
public class BroadcastCounter {
	  private Thread t;
	  private AtomicLong counter = new AtomicLong();
	  private List<DeferredResult<String>> subscribedClient = Collections.synchronizedList(new ArrayList<DeferredResult<String>>());

	  public BroadcastCounter() {
	     t = new Thread(
	             new Runnable() {
	               @Override
	                    public void run() {
	                                while(true) {
	                             counter.incrementAndGet();
	                                       if (counter.get() > Long.MAX_VALUE - 100) {
	                                       counter.set(0);
	                              }
	                                       try {
	                                  Thread.sleep(10000);
	                              } catch (InterruptedException e) {
	                                 
	                              }
	                                      synchronized(subscribedClient) {
	                                    Iterator<DeferredResult<String>> it = subscribedClient.iterator();
	                                                while(it.hasNext()) {
	                                            DeferredResult<String> dr = it.next();
	                                            dr.setResult("{ \"data\" : "+ counter  +"}");
	                                            System.out.println("counter: "+counter);
	                                            it.remove();
	                                     }
	                             }
	                     }
	              }
	           });
	         t.setDaemon(true);
	        t.setName("BroadcastDeferredThread");
	        t.start();
	   }


	   public void addSubscribed(DeferredResult<String> client) {
	       synchronized(subscribedClient) {
	           subscribedClient.add(client);
	      }
	  }
	  /* private void templateProcess(Map<String, Object> parameterMap) {
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
				file = new FileWriter ("D:\\dest\\templates\\temp.txt",true);
				bw = new BufferedWriter(file); 
				pw = new PrintWriter(bw);
				cfg.setDirectoryForTemplateLoading(new File("D:\\files\\templates"));
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
		public String loadTestData(){
			String status="IN PROGRESS";
			FileInputStream excelFile;
			XSSFRow row;
			XSSFCell cell;
			XSSFCell cellVal;
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			try {
				excelFile = new FileInputStream(new File("D:\\files\\TestData.xlsx"));
			
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
								
								 * case XSSFCell.CELL_TYPE_BLANK: xLvalue[r][c] =
								 * "[BLANK]"; break;
								 
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
									
									 * case XSSFCell.CELL_TYPE_BLANK: xLvalue[r][c] =
									 * "[BLANK]"; break;
									 
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
			return "Completed";
		}
		public void copyFile(){
			try {
				 File sourceFile = new File("D:\\dest\\templates\\temp.txt");
				 File destFile = new File("D:\\dest\\temp.txt");
				 Files.copy(sourceFile.toPath(), destFile.toPath());
				
		         } catch (IOException e) {
		 			// TODO Auto-generated catch block
		 			e.printStackTrace();
		 		}	         
		}*/

	}
