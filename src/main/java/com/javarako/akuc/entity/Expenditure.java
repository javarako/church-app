package com.javarako.akuc.entity;

import java.io.File;
import java.util.Date;

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
public class Expenditure extends AuditModel {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Date requestDate;
	private Double amount;
	private Double hstAmount;
	private boolean hst;
	private File evidence;
	private String remarks;
	private String requester;
	private String signature;
	private String treasurerInitial;
	private Integer chequeNo;
	private boolean chequeClear;
	private String payableTo;
	private String note;
	
	@OneToOne(targetEntity = AccountCode.class)
	@JoinColumn(name = "code")
	private AccountCode accountCode;

}
