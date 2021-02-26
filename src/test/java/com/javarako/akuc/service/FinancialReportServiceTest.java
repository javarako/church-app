package com.javarako.akuc.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FinancialReportServiceTest {

	public FinancialReportServiceTest() {
		// TODO Auto-generated constructor stub
	}

    public static void main(String[] args) {
        String excelFilePath = "C:\\Works\\projects\\church-app\\src\\test\\resources\\Financial_Report_Template_2021.xlsx";
         
        try {
            FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
            Workbook workbook = new XSSFWorkbook(inputStream);
 
            Sheet sheet = workbook.getSheetAt(1);
            
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            Cell  cell1 = sheet.getRow(5).createCell(7);
            cell1.setCellValue(456);

            evaluator.evaluateAll();
            
            /**
            Map<Integer, List<String>> data = new HashMap<>();
            int i = 0;
            for (Row row : sheet) {
                data.put(i, new ArrayList<String>());
                for (Cell cell : row) {
                    switch (cell.getCellTypeEnum()) {
                        case STRING: log.info(cell.getStringCellValue()); break;
                        case NUMERIC: log.info(""+cell.getNumericCellValue()); break;
                        case BOOLEAN: log.info(""+cell.getBooleanCellValue()); break;
                        case FORMULA: 
                        	log.info(cell.getCellFormula()); 
                            evaluator.evaluateFormulaCell(cell);
                        	break;
                        default: data.get(new Integer(i)).add(" ");
                    }
                }
                i++;
            }
            **/
            
            inputStream.close();
 
            FileOutputStream outputStream = new FileOutputStream("JavaBooks.xlsx");
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
             
        } catch (IOException | EncryptedDocumentException ex) {
            ex.printStackTrace();
        }
    }
}
