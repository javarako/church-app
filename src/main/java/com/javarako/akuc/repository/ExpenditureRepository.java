package com.javarako.akuc.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.javarako.akuc.entity.Expenditure;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {

	Page<Expenditure> findByRequestDateBetween(Date start, Date end, Pageable pageable);
	Page<Expenditure> findByAccountCodeCommitteeCodeAndRequestDateBetween(String committeeCode, Date start, Date end, Pageable pageable);

}
