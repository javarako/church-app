package com.javarako.akuc.service;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.javarako.akuc.entity.DepositDetail;
import com.javarako.akuc.entity.Offering;
import com.javarako.akuc.entity.ReferenceCode;
import com.javarako.akuc.repository.DepositDetailRepository;
import com.javarako.akuc.repository.OfferingRepository;
import com.javarako.akuc.repository.ReferenceCodeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeeklyOfferingReportService extends ReportFileInfo {

	@Autowired
	OfferingRepository offeringRepository;
	@Autowired
	DepositDetailRepository depositDetailRepository;
	@Autowired
	ReferenceCodeRepository referenceCodeRepository;

	public String getWeeklyOfferingReport(Date start, Date end) {
		deleteOneHourOldReport(off_matcher);

		List<Offering> list = offeringRepository.findByOfferingSundayBetweenOrderByOfferingSundayAscOfferingNumberAsc(start, end);
		
		String fileName = OFF_RPT_FILE + System.currentTimeMillis() + ".csv";

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			
			writer.write(String.format("Offering Report Period: %s ~ %s (Printed: %s)\n",
					DATE_FORMAT.format(start),
					DATE_FORMAT.format(end),
					DATETIME_FORMAT.format(new Date())
					));
			
			writer.write("Offering Sunday, Number, Offering type, Amount type, Amount, Memo\n");
			
			for (Offering offering : list) {
				//offering_sunday, offering_number, offering_type, amount_type, amount, memo
				writer.write(String.format("%s, %d, %s, %s, %.2f, %s\n", 
						DATE_FORMAT.format(offering.getOfferingSunday()),
						offering.getOfferingNumber(),
						offering.getOfferingType(),
						offering.getAmountType(),
						offering.getAmount(),
						offering.getMemo()
						));
			}
			
		    writer.close();
		    
		    return fileName;
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public String getWeeklyOfferingReport(Date offeringSunday) {
		try {
			deleteOneHourOldReport(pdf_weekly_off_matcher);
			baseFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		Map<String, Double> offeringSummary = getOfferingSummary(offeringSunday);

		Optional<DepositDetail> depositDetail = depositDetailRepository.findById(offeringSunday);

		return getPDFReport(offeringSunday, offeringSummary, depositDetail);
	}

	private Map<String, Double> getOfferingSummary(Date offeringSunday) {
		Map<String, Double> offeringSummary = new HashMap<>();

		List<Offering> offerings = offeringRepository.findByOfferingSunday(offeringSunday);

		offerings.stream().forEach(x -> {
			Double offeringTypeSummary = offeringSummary.get(x.getOfferingType());
			if (offeringTypeSummary == null) {
				offeringTypeSummary = x.getAmount();
			} else {
				offeringTypeSummary += x.getAmount();
			}
			offeringSummary.put(x.getOfferingType(), offeringTypeSummary);

			Double amountTypeSummary = offeringSummary.get(x.getAmountType());
			if (amountTypeSummary == null) {
				amountTypeSummary = x.getAmount();
			} else {
				amountTypeSummary += x.getAmount();
			}
			offeringSummary.put(x.getAmountType(), amountTypeSummary);
		});

		return offeringSummary;
	}

	private String getPDFReport(Date offeringSunday, Map<String, Double> offeringSummary,
			Optional<DepositDetail> depositDetail) {

		String newFile = PDF_WEEKLY_OFF_GEN_FILE + System.currentTimeMillis() + ".pdf";
		log.info("Report:{}", newFile);

		try {
			// Create PdfReader instance.
			PdfReader pdfReader = new PdfReader(PDF_WEEKLY_OFF_FILE + ".pdf");

			// Create PdfStamper instance.
			FileOutputStream fileOutputStream = new FileOutputStream(newFile);
			PdfStamper pdfStamper = new PdfStamper(pdfReader, fileOutputStream);

			// Contain the pdf data.
			PdfContentByte pageContentByte = pdfStamper.getOverContent(1);

			printReportDate(pageContentByte, offeringSunday);

			depositDetail.ifPresent(x -> printdepositDetail(pageContentByte, x));

			printSummary(pageContentByte, offeringSummary);

			// Close the pdfStamper.
			pdfStamper.close();
			fileOutputStream.close();

			log.info("OfferingWeeklyReport {} successfully generated.", newFile);
		} catch (Exception e) {
			// e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}

		return newFile;
	}
	
	private void printdepositDetail(PdfContentByte pageContentByte, DepositDetail depositDetail) {
		double cash = (depositDetail.getBill100() * 100) + (depositDetail.getBill050() * 50)
				+ (depositDetail.getBill020() * 20) + (depositDetail.getBill010() * 10) + (depositDetail.getBill005() * 5);
		double total = depositDetail.getChequeTotal() + cash;

		int y = 580;
		// DepositDetail
		printText(pageContentByte, FONT_SIZE_11, 80, y, String.format("[Bank deposit detail]"));
		y -= LINE_SPACE_15;
		printText(pageContentByte, FONT_SIZE_11, 80, y, "Amount total");
		printText(pageContentByte, FONT_SIZE_11, 180, y, String.format("%7.2f", total));
		y -= LINE_SPACE_15;
		printText(pageContentByte, FONT_SIZE_11, 80, y, "Cheque total");
		printText(pageContentByte, FONT_SIZE_11, 180, y, String.format("%7.2f", depositDetail.getChequeTotal()));
		y -= LINE_SPACE_15;
		printText(pageContentByte, FONT_SIZE_11, 80, y, "US cheque total");
		printText(pageContentByte, FONT_SIZE_11, 180, y, String.format("%7.2f", depositDetail.getUsChequeTotal()));
		y -= LINE_SPACE_15;
		printText(pageContentByte, FONT_SIZE_11, 80, y, "Cash total");
		printText(pageContentByte, FONT_SIZE_11, 180, y, String.format("%7.2f", cash));
		y -= LINE_SPACE_15;
		printText(pageContentByte, FONT_SIZE_11, 80, y, "US cash total");
		printText(pageContentByte, FONT_SIZE_11, 180, y, String.format("%7.2f", depositDetail.getUsCashTotal()));
		y -= LINE_SPACE_15;
		printText(pageContentByte, FONT_SIZE_11, 80, y, String.format("$100 X %3d", depositDetail.getBill100()));
		printText(pageContentByte, FONT_SIZE_11, 180, y,
				String.format("%7.2f", (double) (100 * depositDetail.getBill100())));
		y -= LINE_SPACE_15;
		printText(pageContentByte, FONT_SIZE_11, 80, y, String.format(" $50 X %3d", depositDetail.getBill050()));
		printText(pageContentByte, FONT_SIZE_11, 180, y,
				String.format("%7.2f", (double) (50 * depositDetail.getBill050())));
		y -= LINE_SPACE_15;
		printText(pageContentByte, FONT_SIZE_11, 80, y, String.format(" $20 X %3d", depositDetail.getBill020()));
		printText(pageContentByte, FONT_SIZE_11, 180, y,
				String.format("%7.2f", (double) (20 * depositDetail.getBill020())));
		y -= LINE_SPACE_15;
		printText(pageContentByte, FONT_SIZE_11, 80, y, String.format(" $10 X %3d", depositDetail.getBill010()));
		printText(pageContentByte, FONT_SIZE_11, 180, y,
				String.format("%7.2f", (double) (10 * depositDetail.getBill010())));
		y -= LINE_SPACE_15;
		printText(pageContentByte, FONT_SIZE_11, 80, y, String.format("  $5 X %3d", depositDetail.getBill005()));
		printText(pageContentByte, FONT_SIZE_11, 180, y,
				String.format("%7.2f", (double) (5 * depositDetail.getBill005())));
		y -= LINE_SPACE_15;
		printText(pageContentByte, FONT_SIZE_11, 80, y, "Coin out");
		printText(pageContentByte, FONT_SIZE_11, 180, y, String.format("%7.2f", (double) depositDetail.getCoinOut()));
		y -= LINE_SPACE_15;
		printText(pageContentByte, FONT_SIZE_11, 80, y, "Coin change in");
		printText(pageContentByte, FONT_SIZE_11, 180, y, String.format("%7.2f", (double) depositDetail.getCoinIn()));
	}

	private void printSummary(PdfContentByte pageContentByte, Map<String, Double> offeringSummary) {
		int typeMaxLength = 35;
		int y = 350;
		printText(pageContentByte, FONT_SIZE_11, 80, y, String.format("[Offering detail by amount type]"));

		// AMOUNT_TYPE
		for (ReferenceCode x : referenceCodeRepository.findByType("AMOUNT_TYPE")) {
			Double amount = offeringSummary.get(x.getValue());
			if (amount != null) {
				y -= LINE_SPACE_15;
				int space = typeMaxLength - x.getViewValue().trim().length();
				printText(pageContentByte, FONT_SIZE_11, 80, y,
						String.format("%-" + space + "s %7.2f", x.getViewValue().trim(), amount));
			}
		}

		y -= (LINE_SPACE_15 * 2);
		printText(pageContentByte, FONT_SIZE_11, 80, y, String.format("[Offering detail by type]"));

		// OFFERING_TYPE
		for (ReferenceCode x : referenceCodeRepository.findByType("OFFERING_TYPE")) {
			Double amount = offeringSummary.get(x.getValue());
			if (amount != null) {
				y -= LINE_SPACE_15;
				int space = typeMaxLength - x.getViewValue().trim().length();
				printText(pageContentByte, FONT_SIZE_11, 80, y,
						String.format("%-" + space + "s %7.2f", x.getViewValue().trim(), amount));
			}
		}

	}

	private void printReportDate(PdfContentByte pageContentByte, Date offeringSunday) {
		// Sunday
		printText(pageContentByte, 12, 150, 626, DATE_FORMAT.format(offeringSunday));
		// Printed date
		printText(pageContentByte, 12, 150, 604, DATETIME_FORMAT.format(new Date()));
	}	
}
