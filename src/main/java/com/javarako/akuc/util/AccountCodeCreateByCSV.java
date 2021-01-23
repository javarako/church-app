package com.javarako.akuc.util;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javarako.akuc.entity.AccountCode;
import com.javarako.akuc.repository.AccountCodeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AccountCodeCreateByCSV {
	
	@Autowired
	AccountCodeRepository accountCodeRepository;

	public static void main(String[] args) {
	}


	public void convert() {
		List<String> list = convertList();
		list.forEach(str -> {
			String[] fields = str.split(",");
			AccountCode accountCode = getAccountCode(fields);
			
			log.debug(accountCode.toString());
			accountCodeRepository.save(accountCode);
			log.debug("saved!");
		});
		
		assertTrue(true);
	}
	
	//1.일반회계 / GENERAL FUND,	01.목회인사 위원회,	01-5111-001,	목회자 사례비
	private AccountCode getAccountCode(String[] fields) {
		AccountCode accountCode = new AccountCode();
		accountCode.setCode(fields[3].trim());
		accountCode.setItem(fields[4].trim());
		accountCode.setFundType(fields[0].trim());
		accountCode.setCommitteeCode(fields[1].trim());
		accountCode.setCommittee(fields[2].trim());
		return accountCode;
	}
	
	private List<String> convertList() {
	    String fileName = "C:\\Works\\projects\\church-app\\src\\test\\java\\com\\javarako\\akuc\\util\\account_code.csv";

        List<String> list = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {

            //br returns as stream and convert it into a List
            list = br.lines().collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
	}

}
