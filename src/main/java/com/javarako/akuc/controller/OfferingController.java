package com.javarako.akuc.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.javarako.akuc.exception.ApiResponseException;
import com.javarako.akuc.model.Offering;
import com.javarako.akuc.repository.OfferingRepository;
import com.javarako.akuc.util.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class OfferingController {

	@Autowired
	OfferingRepository offeringRepository;

	@GetMapping("/offerings")
	public ResponseEntity<Map<String, Object>> getByOfferingSunday(@RequestParam(required = false) Date offeringSunday,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "30") int size) {

		if (offeringSunday == null) {
			offeringSunday = Utils.getSundayFromToday();
		}
		log.info("/offerings?offeringSunday={}", offeringSunday);

		try {
			Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
			Page<Offering> offerings = offeringRepository.findByOfferingSunday(offeringSunday, pageable);
			
			Map<String, Object> response = new HashMap<>();
			response.put("offerings", offerings.getContent());
			response.put("currentPage", offerings.getNumber());
			response.put("totalItems", offerings.getTotalElements());
			response.put("totalPages", offerings.getTotalPages());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Exception occurred during getByOfferingSunday():\n{}", e);
			throw new ApiResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/offerings/{id}")
	public Offering getById(@PathVariable("id") long id) {

		return offeringRepository.findById(id).map(offering -> {
			return offering;
		}).orElseThrow(() -> new ApiResponseException(id + " not found!", HttpStatus.NOT_FOUND));
	}

	@PostMapping("/offerings")
	public Offering create(@RequestBody Offering offering) {
		log.info(offering.toString());
		return offeringRepository.save(offering);
	}

	@PutMapping("/offerings/{id}")
	public Offering update(@PathVariable("id") long id, @RequestBody Offering offering) {

		return offeringRepository.findById(id).map(existingOffering -> {
			offering.setId(existingOffering.getId());
			return offeringRepository.save(offering);
		}).orElseThrow(() -> new ApiResponseException(id + " not found!", HttpStatus.NOT_FOUND));
	}
	
	@DeleteMapping("/offerings/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) {
		offeringRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/offerings")
	public ResponseEntity<HttpStatus> deleteAll() {
		offeringRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.OK);
	}

}