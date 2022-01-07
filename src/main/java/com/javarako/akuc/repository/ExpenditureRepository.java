package com.javarako.akuc.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.javarako.akuc.entity.Expenditure;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {

	List<Expenditure> findByRequestDateBetweenOrderByRequestDateAscAccountCodeCodeAsc(Date start, Date end);
	Page<Expenditure> findByRequestDateBetweenOrderByRequestDateDescIdDesc(Date start, Date end, Pageable pageable);
	Page<Expenditure> findByAccountCodeCommitteeCodeAndRequestDateBetweenOrderByRequestDateDescIdDesc(String committeeCode, Date start, Date end, Pageable pageable);

	@Query("SELECT exp FROM Expenditure exp WHERE exp.requestDate <= ?1 "
			+ "and exp.chequeNo is not null and exp.chequeClear = false order by exp.requestDate")
	List<Expenditure> findOutstandingCheque(Date end);
}
