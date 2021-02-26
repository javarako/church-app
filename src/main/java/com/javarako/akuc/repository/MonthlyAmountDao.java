package com.javarako.akuc.repository;

import java.util.Date;
import java.util.List;

import com.javarako.akuc.entity.MonthlyAmount;

public interface MonthlyAmountDao {

	public List<MonthlyAmount> getMonthlyOfferingAmount(Date start, Date end);
	public List<MonthlyAmount> getMonthlyExpenditureAmount(Date start, Date end);
}
