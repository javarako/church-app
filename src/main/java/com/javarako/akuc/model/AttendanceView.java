package com.javarako.akuc.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AttendanceView {

	private List<AttendanceModel> members = new ArrayList<>();
	private List<AttendanceModel> visitors = new ArrayList<>();
	
	

}
