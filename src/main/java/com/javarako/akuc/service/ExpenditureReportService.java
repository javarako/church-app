package com.javarako.akuc.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javarako.akuc.entity.Expenditure;
import com.javarako.akuc.model.ReportParam;
import com.javarako.akuc.repository.ExpenditureRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExpenditureReportService extends ReportFileInfo {

	@Autowired
	ExpenditureRepository expenditureRepository;

	public String getExpenditureReport(ReportParam param) {
		log.info("ExpenditureReport Generate");
		deleteOneHourOldReport(exp_matcher);

		List<Expenditure> list = null;
		
		if ("Outstanding_Cheque".equalsIgnoreCase(param.getType())) {
			list = expenditureRepository.
					findOutstandingCheque(param.getFromDate(), param.getToDate());
		} else {
			list = expenditureRepository.
					findByRequestDateBetweenOrderByRequestDateAscAccountCodeCodeAsc(
							param.getFromDate(), param.getToDate());
		}
		
		String fileName = EXP_RPT_FILE + System.currentTimeMillis() + ".csv";

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

			writer.write(String.format("%s Period: %s ~ %s (Printed: %s)\n",
					param.getType(),
					DATE_FORMAT.format(param.getFromDate()),
					DATE_FORMAT.format(param.getToDate()),
					DATETIME_FORMAT.format(new Date())
					));
			
			writer.write("Request date, Account #, Item, Committe, Amount, HST, Requestor, Cheque no, Payable to, Discharge, Note, Remarks\n");
			
			for (Expenditure expenditure : list) {
				//offering_sunday, offering_number, offering_type, amount_type, amount, memo
				writer.write(String.format("%s, %s, %s, %s, %.2f, %.2f, %s, %s, %s, %s, %s, %s\n", 
						DATE_FORMAT.format(expenditure.getRequestDate()),
						expenditure.getAccountCode().getCode(),
						expenditure.getAccountCode().getItem(),
						expenditure.getAccountCode().getCommittee(),
						expenditure.getAmount(),
						expenditure.getHstAmount() == null ? 0:expenditure.getHstAmount() ,
						expenditure.getRequester().replaceAll(",", " "),
						expenditure.getChequeNo() == null ? "":expenditure.getChequeNo(),
						expenditure.getPayableTo().replaceAll(",", " "),
						expenditure.getChequeNo() != null && expenditure.isChequeClear() == true ? "Yes": "",
						expenditure.getNote(),
						expenditure.getRemarks().replaceAll(",", " ").replaceAll("\n", ";")
						));
			}
			
		    writer.close();
		    
		    return fileName;
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
}
