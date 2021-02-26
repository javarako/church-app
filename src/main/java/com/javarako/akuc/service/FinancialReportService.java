package com.javarako.akuc.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javarako.akuc.entity.MonthlyAmount;
import com.javarako.akuc.repository.MonthlyAmountDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FinancialReportService {
	
	public static final String EXCEL_EXT = ".xlsx";
	public static final String TEMPLATE_NAME = "Financial_Report_2021";
	public static final String TEMPLATE_FILE = TEMPLATE_NAME + EXCEL_EXT;
	public static final String GEN_FILE = TEMPLATE_NAME + "GEN_";
	
	public static final String GEN_FILE_PATTERN = GEN_FILE + "*.xlsx";
	public static final Path rootPath = Paths.get("./");
	public static final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + GEN_FILE_PATTERN);
	
	private short cellNumberFormat;
	
	public static Map<String, Integer> position = new HashMap<>();
	static {
		position.put("Weekly", 5);
		position.put("MS_fund", 6);
		position.put("Loose", 7);
		position.put("Thanks", 8);
		position.put("Tithe", 9);
		position.put("Easter", 10);
		position.put("Anniversary", 11);
		position.put("Thanksgiving", 12);
		position.put("Christmas", 13);
		position.put("Canvass", 14);
		position.put("Initial", 15);
		position.put("Group_Contr", 16);
		position.put("Miscellaneous", 17);
		position.put("Fellowship", 18);
		position.put("Visitor", 19);
		position.put("Summer_Camp", 20);
		position.put("KL_Grant", 23);
		position.put("UC_Grant", 25);
		position.put("Designated", 26);
		position.put("Trustee", 31);
	}
	
	@Autowired
	MonthlyAmountDao monthlyAmountDao;
	
	public String getFinancialReport(Date start, Date end) {
   
        String newFile = GEN_FILE + System.currentTimeMillis() + EXCEL_EXT;

        try {
        	deleteOneDayOldReport();
        	
            FileInputStream inputStream = new FileInputStream(new File("C:\\Works\\projects\\church-app\\src\\main\\resources\\Financial_Report_2021.xlsx"));
            Workbook workbook = new XSSFWorkbook(inputStream);
 
            DataFormat format = workbook.createDataFormat();
            cellNumberFormat = format.getFormat("#,###,##0.00");
            
            updateOffering(start, end, workbook.getSheetAt(1));
            updateExpense(start, end, workbook.getSheetAt(2));
            
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            evaluator.evaluateAll();
            
            inputStream.close();
 
            FileOutputStream outputStream = new FileOutputStream(newFile);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            
			log.info("OfferingWeeklyReport {} successfully generated.", newFile);

        } catch (IOException | EncryptedDocumentException ex) {
            ex.printStackTrace();
			log.error(ex.getMessage());
			return null;
        }
		
		return newFile;
	}
	
	private void deleteOneDayOldReport() {
		try {
			List<File> reports = Files.walk(rootPath).filter(f -> matcher.matches(f.getFileName())).map(Path::toFile)
					.collect(Collectors.toList());

			long oneDayOld = new Date().getTime() - (24 * 60 * 60 * 1000);

			reports.forEach(f -> {
				if (f.lastModified() < oneDayOld)
					f.deleteOnExit();
			});
		} catch (Exception e) {
			// e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	private void updateOffering(Date start, Date end, Sheet sheetAt) {
		double totalInSystem = 0L;
				
		List<MonthlyAmount> list = monthlyAmountDao.getMonthlyOfferingAmount(start, end);
		for (MonthlyAmount monthlyAmount : list) {
			
			totalInSystem += monthlyAmount.getSubtotal() == null ? 0L : monthlyAmount.getSubtotal();
			
			Integer rowPosition = position.get(monthlyAmount.getType());
			if (rowPosition == null 
					|| monthlyAmount.getMonth() == null
					|| monthlyAmount.getSubtotal() == null) {
				continue;
			}
			
			Cell  cell = sheetAt.getRow(rowPosition).createCell( 6 + monthlyAmount.getMonth() );
            CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, cellNumberFormat);
			cell.setCellValue(monthlyAmount.getSubtotal());
		}
		
		//Total in system
		Cell  cell = sheetAt.getRow(33).createCell(6);
        CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, cellNumberFormat);
		cell.setCellValue(totalInSystem);
	}

	
	private void updateExpense(Date start, Date end, Sheet sheetAt) {
		double totalInSystem = 0L;
		
		List<MonthlyAmount> list = monthlyAmountDao.getMonthlyExpenditureAmount(start, end);
		for (MonthlyAmount monthlyAmount : list) {
			
			totalInSystem += monthlyAmount.getSubtotal() == null ? 0L : monthlyAmount.getSubtotal();

			if (monthlyAmount.getRowPosition() == null 
					|| monthlyAmount.getMonth() == null
					|| monthlyAmount.getSubtotal() == null) {
				continue;
			}
			
			Cell  cell = sheetAt.getRow(monthlyAmount.getRowPosition()).createCell( 6 + monthlyAmount.getMonth() );
            CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, cellNumberFormat);
			cell.setCellValue(monthlyAmount.getSubtotal());
		}
		
		//Total in system
		Cell  cell = sheetAt.getRow(125).createCell(6);
        CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, cellNumberFormat);
		cell.setCellValue(totalInSystem);
	}

}
