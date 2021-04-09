package com.javarako.akuc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javarako.akuc.entity.ActionRecord;
import com.javarako.akuc.entity.DepositDetail;
import com.javarako.akuc.entity.Offering;
import com.javarako.akuc.exception.ApiResponseException;
import com.javarako.akuc.repository.ActionRecordRepository;
import com.javarako.akuc.repository.DepositDetailRepository;
import com.javarako.akuc.repository.OfferingRepository;
import com.javarako.akuc.service.ReportService;
import com.javarako.akuc.util.ActionType;
import com.javarako.akuc.util.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/secure")
public class OfferingController {

	@Autowired
	OfferingRepository offeringRepository;
	@Autowired
	DepositDetailRepository depositDetailRepository;
	@Autowired
	ActionRecordRepository actionRecordRepository;
	@Autowired
	ReportService offeringWeeklyReportService;
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@GetMapping("/offerings")
	@PreAuthorize("hasRole('ROLE_TREASURER')")
	public List<Offering> getByOfferingSunday(@RequestParam(required = false) Date offeringSunday) {

		if (offeringSunday == null) {
			offeringSunday = Utils.getSundayFromToday();
		}
		log.info("/offerings?offeringSunday={}", offeringSunday);

		try {
			return offeringRepository.findByOfferingSunday(offeringSunday);
		} catch (Exception e) {
			log.error("Exception occurred during getByOfferingSunday():\n{}", e);
			e.printStackTrace();
			throw new ApiResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/offerings/{id}")
	@PreAuthorize("hasRole('ROLE_TREASURER')")
	public Offering getById(@PathVariable("id") long id) {

		return offeringRepository.findById(id).map(offering -> {
			return offering;
		}).orElseThrow(() -> new ApiResponseException(id + " not found!", HttpStatus.NOT_FOUND));
	}

	@PostMapping("/offerings")
	@PreAuthorize("hasRole('ROLE_TREASURER')")
	public Offering create(@RequestBody Offering offering) {
		try {
			log.info(offering.toString());
			return offeringRepository.save(offering);			
		} catch (Exception e) {
			log.error(e.getMessage());
			if (e.getCause() != null && e.getCause().getCause() != null) {
				throw new ApiResponseException(e.getCause().getCause().getMessage(), HttpStatus.BAD_REQUEST);
			} else {
				throw new ApiResponseException(offering.getOfferingNumber() + " not saved due to " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@PutMapping("/offerings/{id}")
	@PreAuthorize("hasRole('ROLE_TREASURER')")
	public Offering update(@PathVariable("id") long id, @RequestBody Offering offering) {

		return offeringRepository.findById(id).map(existingOffering -> {
			offering.setId(existingOffering.getId());
			return offeringRepository.save(offering);
		}).orElseThrow(() -> new ApiResponseException(id + " not found!", HttpStatus.NOT_FOUND));
	}
	
	@DeleteMapping("/offerings/{id}")
	@PreAuthorize("hasRole('ROLE_TREASURER')")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) {

		recordDeleteHistory(id);
		offeringRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private void recordDeleteHistory(long id) {
		
		Offering offering = offeringRepository.findById(id).map(existingOffering -> {
			return existingOffering;
		}).orElseThrow(() -> new ApiResponseException(id + " not found!", HttpStatus.NOT_FOUND));
		
		ActionRecord record = new ActionRecord();
		record.setAction(ActionType.DEL);
		record.setTableName(Offering.class.getSimpleName());
		record.setContent(offering.toString());
		
		actionRecordRepository.save(record);
	}
	
	@DeleteMapping("/offerings")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<HttpStatus> deleteAll() {
		offeringRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/*
	 * DepositDetail
	 */
	@GetMapping("/offerings/depositDetail/{id}")
	@PreAuthorize("hasRole('ROLE_TREASURER')")
	public DepositDetail getDepositDetailById(@PathVariable("id") Date id) {

		return depositDetailRepository.findById(id).map(entity -> {
			return entity;
		}).orElse(new DepositDetail());
		//.orElseThrow(() -> new ApiResponseException(id + " not found!", HttpStatus.NOT_FOUND));
	}
	
	@Transactional
	@PostMapping("/offerings/depositDetail")
	@PreAuthorize("hasRole('ROLE_TREASURER')")
	public ResponseEntity<Resource> saveDepositDetail(@RequestBody DepositDetail entry) {
		
		try {
			log.info(entry.toString());
			depositDetailRepository.save(entry);
			
			String fileName = offeringWeeklyReportService.getWeeklyOfferingReport(entry.getOfferingSunday());
			File file = new File(fileName);
			log.info("{} exists():{}", fileName, file.exists());
			
		    return ResponseEntity
		            .ok()
		            .contentLength(file.length())
		            .contentType(
		                    MediaType.parseMediaType("application/octet-stream"))
		            .body(new InputStreamResource(new FileInputStream(file)));

		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e.getMessage());
			if (e.getCause() != null && e.getCause().getCause() != null) {
				throw new ApiResponseException(e.getCause().getCause().getMessage(), HttpStatus.BAD_REQUEST);
			} else {
				throw new ApiResponseException(entry.getOfferingSunday() + " not saved due to " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
	
}