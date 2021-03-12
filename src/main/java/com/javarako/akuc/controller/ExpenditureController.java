package com.javarako.akuc.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javarako.akuc.entity.Expenditure;
import com.javarako.akuc.exception.ApiResponseException;
import com.javarako.akuc.repository.ExpenditureRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = {"http://72.141.29.202:7852","http://localhost:4200"} )
@RestController
@RequestMapping("/api/secure")
public class ExpenditureController {

	@Autowired
	ExpenditureRepository expenditureRepository;

	@GetMapping("/expenditures")
	public ResponseEntity<Map<String, Object>> get(
			@RequestParam(required = false) String committee,
			@RequestParam(required = false) Date beginDate,
			@RequestParam(required = false) Date endDate,
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "30") int size) {

		Calendar end = new GregorianCalendar();

		if (endDate == null) {
			endDate = end.getTime();
		}
		if (beginDate == null) {
			Calendar januaryFirst = new GregorianCalendar(end.get(Calendar.YEAR), 0, 1);
			beginDate = januaryFirst.getTime();
		}
		
		log.info("/expenditures?committeeCode={}, startDate={}, endDate={}", committee,beginDate,endDate);

		try {
			Pageable pageable = PageRequest.of(page, size, Sort.by("requestDate").descending());
			Page<Expenditure> expenditures = null;
			if (Strings.isEmpty(committee)) {
				expenditures = expenditureRepository.
						findByRequestDateBetweenOrderByRequestDateDescIdDesc(beginDate, endDate, pageable);
			} else {
				expenditures = expenditureRepository.
						findByAccountCodeCommitteeCodeAndRequestDateBetweenOrderByRequestDateDescIdDesc(committee, beginDate, endDate, pageable);
			}
			
			Map<String, Object> response = new HashMap<>();
			response.put("items", expenditures.getContent());
			response.put("currentPage", expenditures.getNumber());
			response.put("totalItems", expenditures.getTotalElements());
			response.put("totalPages", expenditures.getTotalPages());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Exception occurred during get():\n{}", e);
			throw new ApiResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/expenditures/{id}")
	public Expenditure getById(@PathVariable("id") long id) {

		return expenditureRepository.findById(id).map(expenditure -> {
			return expenditure;
		}).orElseThrow(() -> new ApiResponseException(id + " not found!", HttpStatus.NOT_FOUND));
	}

	@PostMapping("/expenditures")
	@PreAuthorize("hasRole('ROLE_TREASURER')")
	public Expenditure create(@RequestBody Expenditure expenditure) {
		try {
			log.info(expenditure.toString());
			return expenditureRepository.save(expenditure);			
		} catch (Exception e) {
			log.error(e.getMessage());
			if (e.getCause() != null && e.getCause().getCause() != null) {
				throw new ApiResponseException(e.getCause().getCause().getMessage(), HttpStatus.BAD_REQUEST);
			} else {
				throw new ApiResponseException(expenditure.getAccountCode().getCode() + " not saved due to " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@PutMapping("/expenditures/{id}")
	@PreAuthorize("hasRole('ROLE_TREASURER')")
	public Expenditure update(@PathVariable("id") long id, @RequestBody Expenditure expenditure) {

		return expenditureRepository.findById(id).map(existing -> {
			expenditure.setId(existing.getId());
			return expenditureRepository.save(expenditure);
		}).orElseThrow(() -> new ApiResponseException(id + " not found!", HttpStatus.NOT_FOUND));
	}
	
	@DeleteMapping("/expenditures/{id}")
	@PreAuthorize("hasRole('ROLE_TREASURER')")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) {
		expenditureRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/expenditures")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<HttpStatus> deleteAll() {
		expenditureRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.OK);
	}

}