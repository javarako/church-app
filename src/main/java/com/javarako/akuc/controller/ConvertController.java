package com.javarako.akuc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javarako.akuc.util.AccountCodeCreateByCSV;
import com.javarako.akuc.util.MemerCreateByCSV;
import com.javarako.akuc.util.ReferenceCodeCreateByCSV;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/secure")
public class ConvertController {

	@Autowired
	MemerCreateByCSV memerCreateByCSV;
	@Autowired
	AccountCodeCreateByCSV accountCodeCreateByCSV;
	@Autowired
	ReferenceCodeCreateByCSV referenceCodeCreateByCSV;

	@GetMapping("/convert/member")
	public void convertMember() {
		memerCreateByCSV.convert();
	}

	@GetMapping("/convert/accountCode")
	public void convertAccountCode() {
		accountCodeCreateByCSV.convert();
	}

	@GetMapping("/convert/code")
	public void convertCode() {
		referenceCodeCreateByCSV.convert();
	}

}