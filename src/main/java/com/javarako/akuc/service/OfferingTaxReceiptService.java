package com.javarako.akuc.service;

import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.javarako.akuc.entity.Address;
import com.javarako.akuc.entity.Member;
import com.javarako.akuc.entity.OfferingSummary;
import com.javarako.akuc.exception.ApiResponseException;
import com.javarako.akuc.repository.MemberRepository;
import com.javarako.akuc.repository.OfferingSummaryDao;
import com.javarako.akuc.util.AddressType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OfferingTaxReceiptService extends ReportFileInfo {

	@Autowired
	OfferingSummaryDao offeringSummaryDao;
	@Autowired
	MemberRepository memberRepository;
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param allMember
	 * @param offeringNo
	 * @return
	 */
	public String getOfferingTaxReceipt(Date start, Date end, boolean allMember, int offeringNumber) {

		try {
			deleteOneHourOldReport(tax_matcher);
			baseFont = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		List<OfferingSummary> offeringSummaryList = new ArrayList<OfferingSummary>();
		Map<Integer, Member> memberMap = new HashMap<>();
		
		if (!allMember && offeringNumber > 0) {
			offeringSummaryList = offeringSummaryDao.getOfferingSummaryByNumber(offeringNumber, start, end);
			Member member = memberRepository.findByOfferingNumber(offeringNumber);
			if (member != null) {
				memberMap.put(member.getOfferingNumber(), member);
			}
			
		} else {
			offeringSummaryList = offeringSummaryDao.getOfferingSummaryByNumber(start, end);
			memberMap = getAllMember();
		}
		
		if (CollectionUtils.isEmpty(offeringSummaryList) 
				|| CollectionUtils.isEmpty(memberMap)) {
			throw new ApiResponseException("OfferingTaxReceipt not found!", HttpStatus.NOT_FOUND);
		}

		String copiedFile = copyPDF(offeringSummaryList.size());
		
		return getPDFReport(copiedFile, start, end, offeringSummaryList, memberMap);	
	}

	private Map<Integer, Member> getAllMember() {
		Map<Integer, Member> memberMap = new HashMap<>();

		List<Member> members = memberRepository.findAll();

		members.stream().forEach(x -> {
			memberMap.put(x.getOfferingNumber(), x);
		});

		return memberMap;
	}

	
	public String copyPDF(int count) {
		
		String newFile = PDF_OFF_TAX_GEN_FILE + "T" + System.currentTimeMillis() + ".pdf";
		log.info("Copy a Report:{}", newFile);
		
		try {
			Document document = new Document();
			FileOutputStream fileOutputStream = new FileOutputStream(newFile);
			PdfCopy writer = new PdfCopy(document, fileOutputStream);
			
			document.open();
			
			PdfReader pdfReader = new PdfReader(PDF_OFF_TAX_FILE + ".pdf");
        	
			log.debug(PDF_OFF_TAX_FILE + " pages: " + pdfReader.getNumberOfPages());
			log.debug("Count: "+count);
			
			for(int i = 0; i < count; i++) {
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
			throw new ApiResponseException("Fail to prepare OfferingTaxReceipt! " + e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	
	public String getPDFReport(String copiedFile, Date start, Date end,
			List<OfferingSummary> offeringSummaryList, Map<Integer, Member> memberMap) {
	
		String newFile = PDF_OFF_TAX_GEN_FILE + System.currentTimeMillis() + ".pdf";
		log.info("Generate a Report:{}", newFile);
		
		try {
			// Create BaseFont instance.
			baseFont = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

			// Create PdfReader instance.
			PdfReader pdfReader = new PdfReader(copiedFile);

			// Create PdfStamper instance.
			FileOutputStream fileOutputStream = new FileOutputStream(newFile);
			PdfStamper pdfStamper = new PdfStamper(pdfReader, fileOutputStream);

			for(int page = 0; page < pdfReader.getNumberOfPages(); ) {
				OfferingSummary offeringSummary = offeringSummaryList.get(page);
				if (offeringSummary == null) {
					++page;
					continue;
				}
				log.info("OfferingTaxReceipt generating of No.", offeringSummary.getOfferingNumber());
				
				Member member = memberMap.get(offeringSummary.getOfferingNumber());
				if (member == null) {
					member = new Member();
				}
					
				// Contain the pdf data.
				PdfContentByte pageContentByte = pdfStamper.getOverContent(++page);
				getPDFReport(pageContentByte, start, end, offeringSummary, member);
			}
			
			// Close the pdfStamper.
			pdfStamper.close();
			fileOutputStream.flush();
			fileOutputStream.close();
			
			log.info("OfferingTaxReceipt {} successfully generated.", newFile);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new ApiResponseException("Fail to generate OfferingTaxReceipt! " + e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		return newFile;
	}

	
	private void getPDFReport(PdfContentByte pageContentByte, Date start, Date end,
			OfferingSummary offeringSummary, Member member) 
	{
		String year = DATE_yyyy_FORMAT.format(start);
		String number = String.format("%03d", offeringSummary.getOfferingNumber());
		Optional<Address> addressOpt = member.getAddresses()
				.stream()
				.filter(x -> x.getType() == AddressType.CRA)
				.findAny();
		
		Address address = null;
		
		if (addressOpt.isPresent()) {
			address = addressOpt.get();
		} else {
			address = member.getAddresses().iterator().next();
		}
				
		//year
		printText(pageContentByte, 15, 184, 659, year);
		
		String printingName = StringUtils.isEmpty(member.getCraName()) ?
				member.getName() + (Strings.isEmpty(member.getSpouseName()) ? "" : " & "+member.getSpouseName())
				: member.getCraName();
				
		//Name
		printText(pageContentByte, 11, 93, 633, printingName);
		//Address
		printText(pageContentByte, 11, 93, 620, address.getAddress1());
		//City
		printText(pageContentByte, 11, 93, 607, address.getCity() + ", " + address.getProvince() + " " + address.getPostalCode());

		//print date
		printText(pageContentByte, 11, 480, 620, DATE_MMDDyyyy_FORMAT.format(new Date()));
		//Envelop No
		printText(pageContentByte, 11, 480, 607, number);
		//Receipt No
		printText(pageContentByte, 11, 480, 594, year + number);

		//start date
		printText(pageContentByte, 11, 200, 494, DATE_MMDDyyyy_FORMAT.format(start));
		//end date
		printText(pageContentByte, 11, 265, 494, DATE_MMDDyyyy_FORMAT.format(end));
		//amount
		printText(pageContentByte, 11, 460, 494, NumberFormat.getCurrencyInstance(Locale.US).format(offeringSummary.getTotal()));
	}

}
