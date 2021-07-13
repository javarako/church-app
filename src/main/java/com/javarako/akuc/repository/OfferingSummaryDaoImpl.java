package com.javarako.akuc.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.javarako.akuc.entity.OfferingSummary;

@Repository
public class OfferingSummaryDaoImpl implements OfferingSummaryDao {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<OfferingSummary> getOfferingSummaryByNumber(Date start, Date end) {
        List<OfferingSummary> summary = 
        		manager.createNamedQuery("OfferingSummary", OfferingSummary.class)
        		.setParameter(1, start)
        		.setParameter(2, end)
        		.getResultList();
        return summary;
    }

    @Override
    public List<OfferingSummary> getOfferingSummaryByNumber(int offeringNumber, Date start, Date end) {
        List<OfferingSummary> summary = 
        		manager.createNamedQuery("OfferingSummaryByNumber", OfferingSummary.class)
        		.setParameter(1, offeringNumber)
        		.setParameter(2, start)
        		.setParameter(3, end)
        		.getResultList();
        return summary;
    }

}
