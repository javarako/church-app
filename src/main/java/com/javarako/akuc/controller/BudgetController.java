package com.javarako.akuc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javarako.akuc.entity.Budget;
import com.javarako.akuc.exception.ApiResponseException;
import com.javarako.akuc.repository.BudgetRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/secure")
public class BudgetController {

	@Autowired
	BudgetRepository budgetRepository;

	@GetMapping("/budgets/{year}")
	public List<Budget> getByYear(@PathVariable("year") int year) {

		try {
			return budgetRepository.findByYear(year, Sort.by("accountCode.committee", "accountCode.code"));
		} catch (Exception e) {
			throw new ApiResponseException(year + " budget not found due to " + e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/budgets")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Budget create(@RequestBody Budget budget) {
		log.info(budget.toString());
		budgetRepository.findByYearAndAccountCodeCode(budget.getYear(), budget.getAccountCode().getCode())
				.map(existingBudget -> {
					throw new ApiResponseException(budget.getAccountCode().getCode() + "/"
							+ budget.getAccountCode().getItem() + " already existed!", HttpStatus.BAD_REQUEST);
				});

		try {
			return budgetRepository.save(budget);
		} catch (Exception e) {
			log.error(e.getMessage());
			if (e.getCause() != null && e.getCause().getCause() != null) {
				throw new ApiResponseException(e.getCause().getCause().getMessage(), HttpStatus.BAD_REQUEST);
			} else {
				throw new ApiResponseException(budget.getAccountCode().getItem() + "/"
						+ budget.getAccountCode().getItem() + " not saved due to " + e.getMessage(),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@Transactional
	@PostMapping("/budgets/{year}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Budget> upload(@PathVariable("year") int year, @RequestBody List<Budget> budgets) {
		try {
			log.info(year + " budget upload!");
			budgets.forEach(x -> x.setYear(year));
			budgetRepository.deleteByYear(year);
			return budgetRepository.saveAll(budgets);
		} catch (Exception e) {
			log.error(e.getMessage());
			if (e.getCause() != null && e.getCause().getCause() != null) {
				throw new ApiResponseException(e.getCause().getCause().getMessage(), HttpStatus.BAD_REQUEST);
			} else {
				throw new ApiResponseException(year + " yearly budget not saved due to " + e.getMessage(),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@PutMapping("/budgets/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Budget update(@PathVariable("id") long id, @RequestBody Budget budget) {

		return budgetRepository.findById(id).map(existingBudget -> {
			budget.setId(existingBudget.getId());
			return budgetRepository.save(budget);
		}).orElseThrow(() -> new ApiResponseException(id + " not found!", HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/budgets/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) {
		budgetRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/budgets")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<HttpStatus> deleteAll() {
		budgetRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.OK);
	}

}