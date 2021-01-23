package com.javarako.akuc.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class AttendanceModel {
	
	private Long id;
	private Date date;
	private Long memberId;
	private String name;
	private boolean attendance;
	private String spouseName;
	private boolean spouseAttendance;
	private String visitorName;
	
}

