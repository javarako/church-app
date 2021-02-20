package com.javarako.akuc.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class DepositDetail extends AuditModel {

	private static final long serialVersionUID = 1L;

	@Id
	private Date offeringSunday;
	private double chequeTotal;
	private double usChequeTotal;
	private double usCashTotal;
	private int bill100;
	private int bill050;
	private int bill020;
	private int bill010;
	private int bill005;
	private int coinOut;
	private int coinIn;

}
