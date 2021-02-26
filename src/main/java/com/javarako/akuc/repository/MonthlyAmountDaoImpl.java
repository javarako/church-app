package com.javarako.akuc.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.javarako.akuc.entity.MonthlyAmount;

@Repository
public class MonthlyAmountDaoImpl implements MonthlyAmountDao {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<MonthlyAmount> getMonthlyOfferingAmount(Date start, Date end) {
        List<MonthlyAmount> employees = 
        		manager.createNamedQuery("MonthlyOfferingAmount", MonthlyAmount.class)
        		.setParameter(1, start)
        		.setParameter(2, end)
        		.getResultList();
        return employees;
    }

    @Override
    public List<MonthlyAmount> getMonthlyExpenditureAmount(Date start, Date end) {
        List<MonthlyAmount> employees = 
        		manager.createNamedQuery("MonthlyExpenditureAmount", MonthlyAmount.class)
        		.setParameter(1, start)
        		.setParameter(2, end)
        		.getResultList();
        return employees;
    }

}
