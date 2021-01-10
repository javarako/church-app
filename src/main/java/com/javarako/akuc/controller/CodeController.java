package com.javarako.akuc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javarako.akuc.exception.ApiResponseException;
import com.javarako.akuc.model.AccountCode;
import com.javarako.akuc.model.ReferenceCode;
import com.javarako.akuc.repository.AccountCodeRepository;
import com.javarako.akuc.repository.ReferenceCodeRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/secure")
public class CodeController {

	@Autowired
	AccountCodeRepository accountCodeRepository;
	@Autowired
	ReferenceCodeRepository referenceCodeRepository;

	@GetMapping("/codes/referenceCode/{type}")
	public List<ReferenceCode> getByCode(@PathVariable("type") String type) {

		return referenceCodeRepository.findByType(type);
	}

	@GetMapping("/codes/account/{code}")
	public AccountCode getByAccountCode(@PathVariable("code") String code) {

		return accountCodeRepository.findById(code).map(accountCode -> {
			return accountCode;
		}).orElseThrow(() -> new ApiResponseException(code + " budget code not found!", HttpStatus.NOT_FOUND));
	}
	
}