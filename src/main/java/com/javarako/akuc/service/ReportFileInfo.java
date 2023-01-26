package com.javarako.akuc.service;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ReportFileInfo {

	public static final String EXCEL_EXT = ".xlsx";
	public static final String TEMPLATE_NAME = "Financial_Report";
	public static final String TEMPLATE_FILE = "/" + TEMPLATE_NAME + EXCEL_EXT;
	public static final String GEN_FILE = TEMPLATE_NAME + "GEN_";

	public static final String GEN_FILE_PATTERN = GEN_FILE + "*.xlsx";
	public static final Path rootPath = Paths.get("./");
	public static final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + GEN_FILE_PATTERN);

	public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATE_yyyy_FORMAT = new SimpleDateFormat("yyyy");
	public static final SimpleDateFormat DATE_MMDDyyyy_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	
	public static final String PDF_WEEKLY_OFF_FILE = "WeeklyOfferingReport";
	public static final String PDF_WEEKLY_OFF_GEN_FILE = PDF_WEEKLY_OFF_FILE + "GEN_";
	public static final String PDF_OFF_TAX_FILE = "ContributionStatement";
	public static final String PDF_OFF_TAX_GEN_FILE = PDF_OFF_TAX_FILE + "GEN_";
	
	public static final String EXP_RPT_FILE = "Expenditure_Report_";
	public static final PathMatcher exp_matcher = FileSystems.getDefault().getPathMatcher("glob:" + EXP_RPT_FILE + "*");
	public static final String OFF_RPT_FILE = "Offering_Report_";
	public static final PathMatcher off_matcher = FileSystems.getDefault().getPathMatcher("glob:" + OFF_RPT_FILE + "*");
	public static final String OFF_TAX_FILE = "OfferingTaxReceipt_";
	public static final PathMatcher tax_matcher = FileSystems.getDefault().getPathMatcher("glob:" + PDF_OFF_TAX_GEN_FILE + "*");
	
	public static final String PDF_WEEKLY_OFF_GEN_PATTERN = PDF_WEEKLY_OFF_GEN_FILE + "*.pdf";
	public static final PathMatcher pdf_weekly_off_matcher = FileSystems.getDefault().getPathMatcher("glob:" + PDF_WEEKLY_OFF_GEN_PATTERN);
	
	public static final int FONT_SIZE_11 = 11;
	public static final int LINE_SPACE_15 = 15;

	// Create BaseFont instance.
	public BaseFont baseFont;

	public void deleteOneHourOldReport(PathMatcher matcher) {
		try {
			long oneDayOld = new Date().getTime() - (60 * 60 * 1000);

			Files.walk(rootPath).filter(f -> matcher.matches(f.getFileName()))
				.map(Path::toFile)
				.filter(f -> f.lastModified() < oneDayOld)
				.peek(System.out::println)
				.forEach(File::delete);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	public void printText(PdfContentByte pageContentByte, final float size, final float x, final float y,
			String text) {
		if (text == null) return;
		
		pageContentByte.beginText();
		// Set text font and size.
		pageContentByte.setFontAndSize(baseFont, size);

		pageContentByte.setTextMatrix(x, y);

		// Write text
		pageContentByte.showText(text);
		pageContentByte.endText();
	}
}
