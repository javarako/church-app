package com.javarako.akuc.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.javarako.akuc.util.AmountType;
import com.javarako.akuc.util.OfferingType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/********************************
CREATE TABLE public.offering_archive
(
    member_id bigint,
    name character varying(50) COLLATE pg_catalog."default",
    id bigint,
    offering_date date,
    offering_sunday date,
    offering_number integer,
    offering_type character varying(10) COLLATE pg_catalog."default",
    amount_type character varying(10) COLLATE pg_catalog."default",
    amount double precision,
    created_at time with time zone,
    updated_at time with time zone,
    created_by character varying(50) COLLATE pg_catalog."default",
    updated_by character varying(50) COLLATE pg_catalog."default"
)
 *******************************/

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
	@Enumerated(EnumType.STRING)
	private OfferingType offeringType;
	@Enumerated(EnumType.STRING)
	private AmountType amountType;
	private Double amount;

}