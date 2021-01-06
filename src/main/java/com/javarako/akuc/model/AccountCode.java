package com.javarako.akuc.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/********************************
CREATE TABLE public.account_code
(
    code character(11) COLLATE pg_catalog."default" NOT NULL,
    fund_type character varying(50) COLLATE pg_catalog."default",
    item character varying(50) COLLATE pg_catalog."default",
    committee character varying(50) COLLATE pg_catalog."default",
    created_at time with time zone,
    updated_at time with time zone,
    created_by character varying(50) COLLATE pg_catalog."default",
    updated_by character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT account_code_pkey PRIMARY KEY (code)
)
 *******************************/

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

}
