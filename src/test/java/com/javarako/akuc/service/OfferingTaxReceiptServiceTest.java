package com.javarako.akuc.service;

import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OfferingTaxReceiptServiceTest {

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// Create BaseFont instance.
	public BaseFont baseFont;
	
	public OfferingTaxReceiptServiceTest() {

	}

	public static void main(String[] args) {
		OfferingTaxReceiptServiceTest test = new OfferingTaxReceiptServiceTest();
		//test.updatePDF1();
		String tempFile = test.copyPDF(3);
		test.updatePDF(tempFile);
	}
	
	public String copyPDF(int count) {
		
		String newFile = "ContributionStatement_T" + System.currentTimeMillis() + ".pdf";
		log.info("Report:{}", newFile);
		
		try {
			Document document = new Document();
			FileOutputStream fileOutputStream = new FileOutputStream(newFile);
			PdfCopy writer = new PdfCopy(document, fileOutputStream);
			
			document.open();
			
			PdfReader pdfReader = new PdfReader("ContributionStatement.pdf");
        	
			for(int i = 0; i < count; i++) {
				log.debug("Count: "+i);
				log.debug("Page: " + pdfReader.getNumberOfPages());
				for(int page = 0; page < pdfReader.getNumberOfPages(); ) {
					writer.addPage(writer.getImportedPage(pdfReader, ++page));
				}
	        }
			
			document.close();
			fileOutputStream.close();

			log.info("OfferingTaxReceipt {} successfully copied.", newFile);
			return newFile;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}

		return null;
	}

	
	public void updatePDF(String copiedFile) {
	
		String newFile = "ContributionStatement_" + System.currentTimeMillis() + ".pdf";
		log.info("Report:{}", newFile);
		
		try {
			// Create BaseFont instance.
			baseFont = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

			// Create PdfReader instance.
			PdfReader pdfReader = new PdfReader(copiedFile);

			// Create PdfStamper instance.
			FileOutputStream fileOutputStream = new FileOutputStream(newFile);
			PdfStamper pdfStamper = new PdfStamper(pdfReader, fileOutputStream);

			for(int page = 0; page < pdfReader.getNumberOfPages(); ) {
				// Contain the pdf data.
				PdfContentByte pageContentByte = pdfStamper.getOverContent(++page);
				createPage(pageContentByte, page);
			}
			
			// Close the pdfStamper.
			pdfStamper.close();
			fileOutputStream.flush();
			fileOutputStream.close();
			
			log.info("OfferingTaxReceipt {} successfully generated.", newFile);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}

	}

	private void createPage(PdfContentByte pageContentByte, int page) {
		Calendar cal = new GregorianCalendar(2021, 0, 1);
		
		//page
		printText(pageContentByte, 9, 480, 100, "Page:"+page);
		
		//year
		printText(pageContentByte, 15, 184, 659, "2021");
		
		//Name
		printText(pageContentByte, 11, 93, 633, "Brad Ko & Karen Choe");
		//Address
		printText(pageContentByte, 11, 93, 620, "3 Copland Tr.");
		//City
		printText(pageContentByte, 11, 93, 607, "Aurora Ontario L4G 4S9");

		//print date
		printText(pageContentByte, 11, 480, 620, DATE_FORMAT.format(new Date()));
		//Envelop No
		printText(pageContentByte, 11, 480, 607, "035");
		//Receipt No
		printText(pageContentByte, 11, 480, 594, "20210035");

		//start date
		printText(pageContentByte, 11, 200, 494, DATE_FORMAT.format(cal.getTime()));
		//end date
		printText(pageContentByte, 11, 265, 494, DATE_FORMAT.format(new Date()));
		//amount
		//NumberFormat.getCurrencyInstance().format(12345L);
		printText(pageContentByte, 11, 460, 494, NumberFormat.getCurrencyInstance().format(12340D));
	}
	
	private void printText(PdfContentByte pageContentByte, final float size, final float x, final float y,
			String text) {
				
		pageContentByte.beginText();
		// Set text font and size.
		pageContentByte.setFontAndSize(baseFont, size);

		pageContentByte.setTextMatrix(x, y);

		// Write text
		pageContentByte.showText(text);
		pageContentByte.endText();
	}
}
