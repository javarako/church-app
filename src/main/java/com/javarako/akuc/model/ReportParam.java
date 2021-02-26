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
public class ReportParam {
	
	private String type;
	private Date fromDate;
	private Date toDate;
	
}

