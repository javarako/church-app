package com.javarako.akuc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javarako.akuc.entity.ReferenceCode;
import com.javarako.akuc.repository.ReferenceCodeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ReferenceCodeCreateByCSV {
	
	@Autowired
	ReferenceCodeRepository referenceCodeRepository;

	public static void main(String[] args) {
	}


	public void convert() {
		List<String> list = convertList();
		list.forEach(str -> {
			String[] fields = str.split(",");
			ReferenceCode referenceCode = getReferenceCode(fields);
			
			log.debug(referenceCode.toString());
			referenceCodeRepository.save(referenceCode);
			log.debug("saved!");
		});
	}
	
	private ReferenceCode getReferenceCode(String[] fields) {
		ReferenceCode referenceCode = new ReferenceCode();
		referenceCode.setId(Long.parseLong(fields[0].trim()));
		referenceCode.setType(fields[1].trim());
		referenceCode.setValue(fields[2].trim());
		referenceCode.setViewValue(fields[3].trim());
		return referenceCode;
	}
	
	private List<String> convertList() {
	    String fileName = "C:\\Works\\projects\\church-app\\src\\test\\java\\com\\javarako\\akuc\\util\\codes.csv";

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
