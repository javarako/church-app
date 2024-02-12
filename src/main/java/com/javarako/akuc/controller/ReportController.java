package com.javarako.akuc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javarako.akuc.entity.MonthlyAmount;
import com.javarako.akuc.exception.ApiResponseException;
import com.javarako.akuc.model.ReportParam;
import com.javarako.akuc.repository.MonthlyAmountDao;
import com.javarako.akuc.service.ExpenditureReportService;
import com.javarako.akuc.service.FinancialReportService;
import com.javarako.akuc.service.OfferingTaxReceiptService;
import com.javarako.akuc.service.WeeklyOfferingReportService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping("/api/secure")
@Slf4j
public class ReportController {

	@Autowired
	MonthlyAmountDao monthlyAmountDao;
	@Autowired
	FinancialReportService financialReportService;
	@Autowired
	WeeklyOfferingReportService weeklyOfferingReportService;
	@Autowired
	OfferingTaxReceiptService offeringTaxReceiptService;
	@Autowired
	ExpenditureReportService expenditureReportService;

	@Transactional
	@PostMapping("/report/offering")
	@PreAuthorize("hasRole('ROLE_TREASURER')")
	public ResponseEntity<Resource> getWeeklyOfferingAmount(@RequestBody ReportParam param) {

		try {
			String fileName = weeklyOfferingReportService.getWeeklyOfferingReport(
					param.getFromDate(), param.getToDate(), param.isAllMember(), param.getOfferingNo());

			File file = new File(fileName);
			log.info("{} exists():{}", fileName, file.exists());

			return ResponseEntity.ok().contentLength(file.length())
					.contentType(MediaType.parseMediaType("application/octet-stream"))
					.body(new InputStreamResource(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			log.error(e.getMessage());
			throw new ApiResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@PostMapping("/report/taxreceipt")
	@PreAuthorize("hasRole('ROLE_TREASURER')")
	public ResponseEntity<Resource> getOfferingTaxReceipt(@RequestBody ReportParam param) {

		try {
			String fileName = offeringTaxReceiptService.getOfferingTaxReceipt(
					param.getFromDate(), param.getToDate(), param.isAllMember(), param.getOfferingNo());

			File file = new File(fileName);
			log.info("{} exists():{}", fileName, file.exists());

			return ResponseEntity.ok().contentLength(file.length())
					.contentType(MediaType.parseMediaType("application/octet-stream"))
					.body(new InputStreamResource(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			log.error(e.getMessage());
			throw new ApiResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@Transactional
	@PostMapping("/report/expenditure")
	@PreAuthorize("hasRole('ROLE_TREASURER')")
	public ResponseEntity<Resource> getExpenditureAmount(@RequestBody ReportParam param) {
		try {
			String fileName = expenditureReportService.getExpenditureReport(param);

			File file = new File(fileName);
			log.info("{} exists():{}", fileName, file.exists());

			return ResponseEntity.ok().contentLength(file.length())
					.contentType(MediaType.parseMediaType("application/octet-stream"))
					.body(new InputStreamResource(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			log.error(e.getMessage());
			throw new ApiResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public List<MonthlyAmount> getMonthlyOfferingAmount() {
		Calendar end = new GregorianCalendar();
		Calendar start = new GregorianCalendar(end.get(Calendar.YEAR), 0, 1);

		return monthlyAmountDao.getMonthlyOfferingAmount(start.getTime(), end.getTime());
	}

	@GetMapping("/report/expenditure")
	@PreAuthorize("hasRole('ROLE_TREASURER')")
	public List<MonthlyAmount> getMonthlyExpenditureAmount() {
		Calendar end = new GregorianCalendar();
		Calendar start = new GregorianCalendar(end.get(Calendar.YEAR), 0, 1);

		return monthlyAmountDao.getMonthlyExpenditureAmount(start.getTime(), end.getTime());
	}

	@Transactional
	@PostMapping("/report/finance")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Resource> getMonthlyFinancialReport(@RequestBody ReportParam param) {
		// Calendar end = new GregorianCalendar();
		// Calendar start = new GregorianCalendar(end.get(Calendar.YEAR), 0, 1);
		log.info(param.getFromDate().toString());

		try {
			String fileName = financialReportService.getFinancialReport(param.getFromDate(), param.getToDate());

			File file = new File(fileName);
			log.info("{} exists():{}", fileName, file.exists());

			return ResponseEntity.ok().contentLength(file.length())
					.contentType(MediaType.parseMediaType("application/octet-stream"))
					.body(new InputStreamResource(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			log.error(e.getMessage());
			throw new ApiResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ApiResponseException ae) {
			throw ae;
		}
	}

}