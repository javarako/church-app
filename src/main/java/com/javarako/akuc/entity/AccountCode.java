package com.javarako.akuc.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class AccountCode extends AuditModel {

	private static final long serialVersionUID = 1L;

	@Id
	private String code;
	private String item;
	private String fundType;
	private String committeeCode;
	private String committee;
	private Integer rowPosition;

}
