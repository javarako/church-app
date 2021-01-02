package com.javarako.akuc.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.javarako.akuc.model.Member;
import com.javarako.akuc.repository.MemberRepository;
import com.javarako.akuc.repository.OfferingArchiveRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class MemberController {

	@Autowired
	MemberRepository memberRepository;
	@Autowired
	OfferingArchiveRepository offeringArchiveRepository;

	@GetMapping("/members")
	public ResponseEntity<Map<String, Object>> getAll(@RequestParam(required = false) String name,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "nickName") String sortBy) {

		log.info("/members?name={}", name);

		try {
			Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
			Page<Member> members;
			if (Strings.isEmpty(name)) {
				members = memberRepository.findAll(pageable);
			} else {
				members = memberRepository
						.findByNameContainsIgnoreCaseOrNickNameContainsIgnoreCaseOrSpouseNameContainsIgnoreCaseOrSpouseNickNameContainsIgnoreCase(
								name, name, name, name, pageable);
			}

			Map<String, Object> response = new HashMap<>();
			response.put("members", members.getContent());
			response.put("currentPage", members.getNumber());
			response.put("totalItems", members.getTotalElements());
			response.put("totalPages", members.getTotalPages());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Exception occurred during getAllMembers():\n{}", e);
			throw new ApiResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/members/{id}")
	public Member getById(@PathVariable("id") long id) {

		return memberRepository.findById(id).map(member -> {
			return member;
		}).orElseThrow(() -> new ApiResponseException(id + " not found!", HttpStatus.NOT_FOUND));
	}

	@GetMapping("/members/offeringArchive")
	public void getOfferingArchive(@RequestParam(required = true) int offeringNumber,
			@RequestParam(required = true) int year) {

		Calendar start = new GregorianCalendar(year, 0, 1);
		Calendar end = new GregorianCalendar(year, 11, 31);
		SimpleDateFormat yyyyMMDD = new SimpleDateFormat("yyyyMMdd");

		offeringArchiveRepository.getOfferingArchiving(offeringNumber, yyyyMMDD.format(start.getTime()),
				yyyyMMDD.format(end.getTime()));
	}

	@PostMapping("/members")
	public Member create(@RequestBody Member member) {
		return memberRepository.save(member);
	}

	@PutMapping("/members/{id}")
	public Member update(@PathVariable("id") long id, @RequestBody Member member) {

		return memberRepository.findById(id).map(existingMember -> {
			member.setMemberId(existingMember.getMemberId());
			return memberRepository.save(member);
		}).orElseThrow(() -> new ApiResponseException(id + " not found!", HttpStatus.NOT_FOUND));
	}

	@PutMapping("/members/offeringNumber")
	public Member updateOfferingNumber(@RequestBody Member member) {

		if (member.getOfferingNumber() != null) {
			if (memberRepository.findByOfferingNumber(member.getOfferingNumber()) != null) {
				throw new ApiResponseException(
						"Offering # " + member.getOfferingNumber() + " already occupied! Back to previous #",
						HttpStatus.BAD_REQUEST);
			}
		}

		return memberRepository.save(member);
	}

	@DeleteMapping("/members/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) {
		memberRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/members")
	public ResponseEntity<HttpStatus> deleteAll() {
		memberRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.OK);
	}

}