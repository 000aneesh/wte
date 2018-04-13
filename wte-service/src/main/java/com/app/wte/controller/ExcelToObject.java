package com.app.wte.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.app.wte.model.CsvDTO;
import com.app.wte.model.DataDTO;

public class ExcelToObject {

	// private static final String PROPERTY_EXCEL_SOURCE_FILE_PATH =
	// "excel.file.path";

	public static List<CsvDTO> excelReader(String excelPath)
			throws IOException, InvalidFormatException, IllegalStateException {
		List<CsvDTO> list = new ArrayList<CsvDTO>();
		Workbook workbook = WorkbookFactory.create(new File(excelPath));
		System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
		Sheet sheet = workbook.getSheetAt(0);
		DataFormatter dataFormatter = new DataFormatter();
		Iterator<Row> rowIterator = sheet.rowIterator();
		while (rowIterator.hasNext()) {
			CsvDTO cvs = new CsvDTO();
			String key = null;
			String url = null;
			String xpath = null;
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				String cellValue = dataFormatter.formatCellValue(cell);
				System.out.print(cellValue + "\t");

				if (cell.getColumnIndex() == 0) {
					key = cellValue;
				}
				if (cell.getColumnIndex() == 1) {
					url = cellValue;
				}
				if (cell.getColumnIndex() == 2) {
					xpath = cellValue;
				}

			}
			DataDTO data = new DataDTO();
			data.setUrl(url);
			data.setXpath(xpath);
			Map<String, DataDTO> cvsObj = new HashMap<String, DataDTO>();
			cvsObj.put(key, data);
			cvs.setKey(cvsObj);
			list.add(cvs);
			System.out.println(list);
		}

		workbook.close();
		return list;
	}

	// private static void printCellValue(Cell cell) {
	// switch (cell.getCellType()) {
	// case Cell.CELL_TYPE_BOOLEAN:
	// System.out.print(cell.getBooleanCellValue());
	// break;
	// case Cell.CELL_TYPE_STRING:
	// System.out.print(cell.getRichStringCellValue().getString());
	// break;
	// case Cell.CELL_TYPE_NUMERIC:
	// if (DateUtil.isCellDateFormatted(cell)) {
	// System.out.print(cell.getDateCellValue());
	// } else {
	// System.out.print(cell.getNumericCellValue());
	// }
	// break;
	// case Cell.CELL_TYPE_FORMULA:
	// System.out.print(cell.getCellFormula());
	// break;
	// case Cell.CELL_TYPE_BLANK:
	// System.out.print("");
	// break;
	// default:
	// System.out.print("");
	// }
	//
	// System.out.print("\t");
	// }

}