package com.javarako.akuc.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/********************************
CREATE TABLE public.offering
(
    id bigint NOT NULL,
    offering_date date NOT NULL,
    offering_number integer NOT NULL,
    offering_type character varying(10) COLLATE pg_catalog."default",
    amount_type character varying(10) COLLATE pg_catalog."default",
    amount double precision,
    created_at time with time zone,
    updated_at time with time zone,
    created_by character varying(50) COLLATE pg_catalog."default",
    updated_by character varying(50) COLLATE pg_catalog."default",
    offering_sunday date,
)
 *******************************/

@Getter
@Setter
@ToString
@Entity
public class Offering extends AuditModel {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Date offeringDate;
	private Date offeringSunday;
	private Integer offeringNumber;
	private String offeringType;
	private String amountType;
	private Double amount;

}