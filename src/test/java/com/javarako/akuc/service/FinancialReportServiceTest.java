package com.javarako.akuc.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.sql.DataSource;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import com.javarako.akuc.exception.ApiResponseException;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class FinancialReportServiceTest {

	@Autowired
	DataSource dataSource;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	FinancialReportService financialReportService;
	
	@Test
	public void dummyTest() {
		assertThat(true).isTrue();
	}
	
	//@Test
	public void financialReportServiceTest() {
		Calendar from = new GregorianCalendar(2022, 0, 1);
		Calendar to = new GregorianCalendar(2022, 0, 31);
		
		try {
			String fileName = financialReportService.getFinancialReport(from.getTime(), to.getTime());
			assertThat(fileName).isNotNull();
			
			File file = new File(fileName);
			log.info("{} exists():{}", fileName, file.exists());

		} catch (Exception e) {
			// e.printStackTrace();
			log.error(e.getMessage());
			throw new ApiResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    public static void main(String[] args) {
        String excelFilePath = "C:\\Works\\projects\\church-app\\src\\test\\resources\\Financial_Report_Template_2022.xlsx";
         
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
