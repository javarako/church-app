package com.javarako.akuc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
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

import com.javarako.akuc.entity.Attendance;
import com.javarako.akuc.entity.Member;
import com.javarako.akuc.exception.ApiResponseException;
import com.javarako.akuc.model.AttendanceModel;
import com.javarako.akuc.model.AttendanceView;
import com.javarako.akuc.repository.AttendanceRepository;
import com.javarako.akuc.repository.MemberRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/secure")
public class AttendanceController {

	@Autowired
	MemberRepository memberRepository;
	@Autowired
	AttendanceRepository attendanceRepository;
	
	@GetMapping("/attendances")
	@PreAuthorize("hasRole('ROLE_MEMBERSHIP') or hasRole('ROLE_TREASURER')")
	public ResponseEntity<Map<String, Object>> getByDate(
			@RequestParam(required = true) Date sunday,
			@RequestParam(required = false) String name,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "nickName") String sortBy) {

		log.info("/attendances?sunday={}, name={}", sunday, name);
		sunday.setHours(0);
		sunday.setMinutes(0);
		sunday.setSeconds(0);
		
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

			Map<Long, Attendance> attendances = 
					attendanceRepository.findByDate(sunday).stream()
						.collect(Collectors.toMap(Attendance::getMemberId, attendance -> attendance));
			
			AttendanceView view = getAttendanceView(sunday, members, attendances);
			
			Map<String, Object> response = new HashMap<>();
			response.put("members", view.getMembers());
			response.put("visitors", view.getVisitors());
			response.put("currentPage", members.getNumber());
			response.put("totalItems", members.getTotalElements());
			response.put("totalPages", members.getTotalPages());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Exception occurred during getAllMembers():\n{}", e);
			throw new ApiResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private AttendanceView getAttendanceView(Date sunday, Page<Member> members, Map<Long, Attendance> attendances) {
		List<AttendanceModel> memberList = members.stream().map(x -> {
			Attendance attendance = attendances.get(x.getMemberId());
			if (attendance != null) {
				return new AttendanceModel(
						attendance.getId(), sunday, x.getMemberId(),
						x.getNickName() + (!Strings.isEmpty(x.getNickName()) && !Strings.isEmpty(x.getName()) ? "/":"") + x.getName(), 
						attendance.isAttendance(), 
						x.getSpouseNickName() + (!Strings.isEmpty(x.getSpouseNickName()) && !Strings.isEmpty(x.getSpouseName()) ? "/":"") + x.getSpouseName(), 
						attendance.isAttendance(), 
						null);
			} else {
				return new AttendanceModel(
						null, sunday, x.getMemberId(), 
						x.getNickName() + (!Strings.isEmpty(x.getNickName()) && !Strings.isEmpty(x.getName()) ? "/":"") + x.getName(), 
						false, 
						x.getSpouseNickName() + (!Strings.isEmpty(x.getSpouseNickName()) && !Strings.isEmpty(x.getSpouseName()) ? "/":"") + x.getSpouseName(), 
						false, 
						null);
			}
		}).collect(Collectors.toList());
		
		List<AttendanceModel> visitorList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(attendances)) {
			visitorList = attendances.values().stream()
				.filter(x -> !Strings.isEmpty(x.getVisitorName()))
				.map(x -> {
					return new AttendanceModel(
							x.getId(), sunday, null, 
							null, 
							x.isAttendance(), 
							null, 
							x.isAttendance(), 
							x.getVisitorName());
				})
				.collect(Collectors.toList());
		}
		
		AttendanceView view = new AttendanceView();
		view.setMembers(memberList);
		view.setVisitors(visitorList);
		
		return view;
	}

	@GetMapping("/attendances/{id}")
	public Attendance getById(@PathVariable("id") long id) {

		return attendanceRepository.findById(id).map(x -> {
			return x;
		}).orElseThrow(() -> new ApiResponseException(id + " not found!", HttpStatus.NOT_FOUND));
	}

	@PostMapping("/attendances")
	@PreAuthorize("hasRole('ROLE_MEMBERSHIP') or hasRole('ROLE_USER')")
	public Attendance create(@RequestBody AttendanceModel x) {
		return attendanceRepository.save(mapAttendance(x));
	}

	private Attendance mapAttendance(AttendanceModel x) {
		Attendance attendance = new Attendance();
		attendance.setId(x.getId());
		attendance.setDate(x.getDate());
		attendance.setMemberId(x.getMemberId());
		attendance.setAttendance(x.isAttendance());
		attendance.setSpouseAttendance(x.isSpouseAttendance());
		attendance.setVisitorName(x.getVisitorName());
		return attendance;
	}
	
	@PutMapping("/attendances/{id}")
	public Attendance update(@PathVariable("id") long id, @RequestBody AttendanceModel x) {
		return attendanceRepository.findById(id).map(existing-> {
			Attendance attendance = mapAttendance(x);
			attendance.setId(existing.getId());
			return attendanceRepository.save(attendance);
		}).orElseThrow(() -> new ApiResponseException(id + " not found!", HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/attendances/{id}")
	@PreAuthorize("hasRole('ROLE_MEMBERSHIP')")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) {
		attendanceRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/attendances")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<HttpStatus> deleteAll() {
		attendanceRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.OK);
	}

}