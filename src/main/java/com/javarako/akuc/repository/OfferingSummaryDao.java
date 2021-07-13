package com.javarako.akuc.repository;

import java.util.Date;
import java.util.List;

import com.javarako.akuc.entity.OfferingSummary;

public interface OfferingSummaryDao {

	public List<OfferingSummary> getOfferingSummaryByNumber(Date start, Date end);
	public List<OfferingSummary> getOfferingSummaryByNumber(int offeringNumber, Date start, Date end);
}
