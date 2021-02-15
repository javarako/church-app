package com.javarako.akuc.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Budget extends AuditModel {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Integer year;
	private Double lastYearBudget;
	private Double lastYearActual;
	private Double budget;
	private Double actual;
	private String note;
	
	@OneToOne(targetEntity = AccountCode.class)
	@JoinColumn(name = "code")
	private AccountCode accountCode;


}
