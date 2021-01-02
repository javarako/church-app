package com.javarako.akuc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.javarako.akuc.model.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

	List<Budget> findByYear(Integer year, Sort sort);
	Optional<Budget> findByYearAndAccountCodeCode(Integer year, String code);
	void deleteByYear(Integer year);
}
