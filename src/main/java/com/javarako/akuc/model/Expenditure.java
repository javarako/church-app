package com.javarako.akuc.model;

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

/********************************
CREATE TABLE public.expenditure
(
    id bigint NOT NULL,
    code character(11) COLLATE pg_catalog."default",
    request_date date,
    amount double precision,
    evidence bytea,
    remarks character varying(200) COLLATE pg_catalog."default",
    requester character varying(50) COLLATE pg_catalog."default",
    signature character varying(50) COLLATE pg_catalog."default",
    "treasurer_initial " character varying(50) COLLATE pg_catalog."default",
    cheque_no smallint,
    payable_to character varying(50) COLLATE pg_catalog."default",
    note character varying(200) COLLATE pg_catalog."default",
    created_at time with time zone,
    updated_at time with time zone,
    created_by character varying(50) COLLATE pg_catalog."default",
    updated_by character varying(50) COLLATE pg_catalog."default",
)
 *******************************/

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
	private File evidence;
	private String remarks;
	private String requester;
	private String signature;
	private String treasurerInitial;
	private Integer chequeNo;
	private String payableTo;
	private String note;
	
	@OneToOne(targetEntity = AccountCode.class)
	@JoinColumn(name = "code")
	private AccountCode accountCode;


}
