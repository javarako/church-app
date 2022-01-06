package com.javarako.akuc.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javarako.akuc.entity.Expenditure;
import com.javarako.akuc.entity.MonthlyAmount;
import com.javarako.akuc.repository.ExpenditureRepository;
import com.javarako.akuc.repository.MonthlyAmountDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FinancialReportService extends ReportFileInfo{


	private short cellNumberFormat;

	/***
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
		position.put("UC_Grant_Refugee", 24);
		position.put("UC_Grant_Other", 25);
		position.put("Designated", 26);
		position.put("Trustee", 31);
	}
	***/
	
	@Autowired
	MonthlyAmountDao monthlyAmountDao;
	@Autowired
	ExpenditureRepository expenditureRepository;
	
	public String getFinancialReport(Date start, Date end) {

		String newFile = GEN_FILE + System.currentTimeMillis() + EXCEL_EXT;

		try {
			deleteOneHourOldReport(matcher);

			InputStream inputStream = getClass().getResourceAsStream(TEMPLATE_FILE);
			Workbook workbook = new XSSFWorkbook(inputStream);

			DataFormat format = workbook.createDataFormat();
			cellNumberFormat = format.getFormat("#,###,##0.00");

			CellStyle cellStyle = workbook.createCellStyle();
			updateReportDate(start, end, workbook.getSheetAt(0), cellStyle);
			updateOffering(start, end, workbook.getSheetAt(1));
			updateExpense(start, end, workbook.getSheetAt(2));
			updateCashflow(start, end, workbook.getSheetAt(3));

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

	private void updateReportDate(Date start, Date end, Sheet sheetAt, CellStyle cellStyle) {
		String reportDate = String.format("Report period: %s ~ %s   (Printed at %s)", simpleDateFormat.format(start),
				simpleDateFormat.format(end), dateFormat.format(new Date()));
		
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		
		Cell cell = sheetAt.getRow(2).createCell(0);
		cell.setCellValue(reportDate);
		cell.setCellStyle(cellStyle);
	}


	private void updateOffering(Date start, Date end, Sheet sheetAt) {
		double totalInSystem = 0L;

		List<MonthlyAmount> list = monthlyAmountDao.getMonthlyOfferingAmount(start, end);
		for (MonthlyAmount monthlyAmount : list) {

			totalInSystem += monthlyAmount.getSubtotal() == null ? 0L : monthlyAmount.getSubtotal();

			if (monthlyAmount.getRowPosition() == null || monthlyAmount.getMonth() == null
					|| monthlyAmount.getSubtotal() == null) {
				continue;
			}

			Cell cell = sheetAt.getRow(monthlyAmount.getRowPosition()).createCell(6 + monthlyAmount.getMonth());
			CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, cellNumberFormat);
			cell.setCellValue(cell.getNumericCellValue() + monthlyAmount.getSubtotal());
		}

		// Total in system
		Cell cell = sheetAt.getRow(33).createCell(6);
		CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, cellNumberFormat);
		cell.setCellValue(totalInSystem);
	}

	private void updateExpense(Date start, Date end, Sheet sheetAt) {
		double totalInSystem = 0L;

		List<MonthlyAmount> list = monthlyAmountDao.getMonthlyExpenditureAmount(start, end);
		for (MonthlyAmount monthlyAmount : list) {

			totalInSystem += monthlyAmount.getSubtotal() == null ? 0L : monthlyAmount.getSubtotal();

			if (monthlyAmount.getRowPosition() == null || monthlyAmount.getMonth() == null
					|| monthlyAmount.getSubtotal() == null) {
				continue;
			}

			Cell cell = sheetAt.getRow(monthlyAmount.getRowPosition()).createCell(6 + monthlyAmount.getMonth());
			CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, cellNumberFormat);
			cell.setCellValue(monthlyAmount.getSubtotal());
		}

		// Total in system
		Cell cell = sheetAt.getRow(125).createCell(6);
		CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, cellNumberFormat);
		cell.setCellValue(totalInSystem);
	}

	private void updateCashflow(Date start, Date end, Sheet sheetAt) {
		List<Expenditure> list = expenditureRepository.
				findOutstandingCheque(start, end);
		
		double outstandingCheques = list.stream()
					.mapToDouble(x -> x.getAmount())
					.sum();
		
		Cell cell = sheetAt.getRow(17).createCell(3);
		CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, cellNumberFormat);
		cell.setCellValue(outstandingCheques);
	}


}
