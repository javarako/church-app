package com.javarako.akuc.model;

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
CREATE TABLE public.budget
(
    id bigint NOT NULL,
    year smallint,
    code character(11) COLLATE pg_catalog."default",
    last_year_budget double precision,
    lasy_year_actual double precision,
    budget double precision,
    actual double precision,
    note character varying(200) COLLATE pg_catalog."default",
    created_at time with time zone,
    updated_at time with time zone,
    created_by character varying(50) COLLATE pg_catalog."default",
    updated_by character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT budget_pkey PRIMARY KEY (id)
)
 *******************************/

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
