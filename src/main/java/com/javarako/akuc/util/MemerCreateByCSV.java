package com.javarako.akuc.util;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javarako.akuc.model.Address;
import com.javarako.akuc.model.Member;
import com.javarako.akuc.model.Phone;
import com.javarako.akuc.repository.MemberRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MemerCreateByCSV {
	
	@Autowired
	MemberRepository memberRepository;

	public static void main(String[] args) {
	}


	//0name-KO	1spouse-KO	2name-EN	3spouse-EN	
	//4Address	5City	6Postal	7primary-email	8secondary-email	
	//9location	10phone	11type	
	public void convert() {
		List<String> list = convertList();
		list.forEach(str -> {
			String[] fields = str.split(",");
			Member member = getMember(fields);
			
			Address address = getAddress(fields);
			member.getAddresses().add(address);
			
			Set<Phone> phones = getPhones(fields);
			member.getPhones().addAll(phones);
			log.debug(member.toString());
			memberRepository.save(member);
			log.debug("saved!");
		});
		
		assertTrue(true);
	}
	
	private Set<Phone> getPhones(String[] fields) {
		Set<Phone> phones = new HashSet<>();
		
		if (fields.length > 10) {
			Phone phone = new Phone();
			phone.setNumber(fields[10].trim());
			if (fields.length > 11) {
				phone.setType(getPhoneType(fields[11].trim()));
			} else {
				phone.setType(PhoneType.Home);
			}
			phone.setCountryCode(1);
			phones.add(phone);
		}
		
		if (fields.length > 12) {
			Phone phone = new Phone();
			phone.setNumber(fields[12].trim());
			if (fields.length > 13) {
				phone.setType(getPhoneType(fields[13].trim()));
			} else {
				phone.setType(PhoneType.Home);
			}
			phone.setCountryCode(1);
			phones.add(phone);
		}
		return phones;
	}

	private PhoneType getPhoneType(String str) {
		try {
			if (str.contains("M")) {
				return PhoneType.Cell;
			} else if (str.contains("R")) {
				return PhoneType.Home;
			} else {
				return PhoneType.Home;
			}
		} catch (Exception e) {
			return PhoneType.Home;
		}
	}

	private Address getAddress(String[] fields) {
		Address address = new Address();
		address.setAddress1(fields[4].trim());
		address.setCity(fields[5].trim());
		address.setPostalCode(fields[6].trim());
		address.setProvince(ProvinceType.ON);
		address.setCountry(CountryType.CA);
		address.setType(AddressType.Home);
		address.setMailingAddress(true);
		return address;
	}

	private Member getMember(String[] fields) {
		Member member = new Member();
		member.setName(fields[2].trim());
		member.setNickName(fields[0].trim());
		member.setSpouseName(fields[3].trim());
		member.setSpouseNickName(fields[1].trim());
		member.setPrimaryEmail(fields[7].trim());
		member.setSecondaryEmail(fields[8].trim());
		member.setLocationCode(getLocationCode(fields[9].trim()));
		return member;
	}
	
	private LocationCode getLocationCode(String str) {
		switch (str) {
		case "1구역":
			return LocationCode.Location_1;
		case "2구역":
			return LocationCode.Location_2;
		case "3구역":
			return LocationCode.Location_3;
		case "4구역":
			return LocationCode.Location_4;
		case "5구역":
			return LocationCode.Location_5;
		case "6구역":
			return LocationCode.Location_6;

		default:
			return null;
		}
	}
	
	private List<String> convertList() {
	    String fileName = "C:\\Works\\projects\\church-app\\src\\test\\java\\com\\javarako\\akuc\\util\\member_sample.csv";

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
