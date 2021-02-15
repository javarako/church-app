package com.javarako.akuc.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class OfferingArchive extends AuditModel {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Long memberId;
	private Long offeringId;
	private String name;
	private Date offeringDate;
	private Date offeringSunday;
	private Long offeringNumber;
	private String offeringType;
	private String amountType;
	private Double amount;
	private String memo;

}