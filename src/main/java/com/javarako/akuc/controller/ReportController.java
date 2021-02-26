package com.javarako.akuc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javarako.akuc.entity.MonthlyAmount;
import com.javarako.akuc.repository.MonthlyAmountDao;
import com.javarako.akuc.service.FinancialReportService;

@CrossOrigin(origins = {"http://72.141.29.202:7852","http://localhost:4200"} )
@RestController
@RequestMapping("/api/secure")
public class ReportController {

	@Autowired
	MonthlyAmountDao monthlyAmountDao;
	@Autowired
	FinancialReportService financialReportService;


	@GetMapping("/report/offering")
	public List<MonthlyAmount> getMonthlyOfferingAmount() {
		Date start = null;
		try {
			start = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(new java.util.Date().getYear())+"-01-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date end = new Date();

		return monthlyAmountDao.getMonthlyOfferingAmount(start, end);
	}

	@GetMapping("/report/expenditure")
	public List<MonthlyAmount> getMonthlyExpenditureAmount() {
		Date start = null;
		try {
			start = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(new java.util.Date().getYear())+"-01-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date end = new Date();

		return monthlyAmountDao.getMonthlyExpenditureAmount(start, end);
	}
	
	@GetMapping("/report/finance")
	public void getMonthlyFinancialRepot() {
		Date start = null;
		try {
			start = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(new java.util.Date().getYear())+"-01-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date end = new Date();
		
		financialReportService.getFinancialReport(start, end);
	}
}