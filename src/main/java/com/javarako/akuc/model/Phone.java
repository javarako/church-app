package com.javarako.akuc.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.javarako.akuc.util.PhoneType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/********************************
CREATE TABLE public.phone
(
    id bigint NOT NULL,
    member_id bigint NOT NULL,
    type character(5) COLLATE pg_catalog."default",
    country_code smallint,
    "number" character(20) COLLATE pg_catalog."default",
    extension smallint,
)
 *******************************/

@Getter
@Setter
@ToString
@Entity
public class Phone extends AuditModel {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Enumerated(EnumType.STRING)
	private PhoneType type;
	private Integer countryCode;
	private String number;
	private String reference;
	
    @ManyToOne(fetch = FetchType.LAZY)
	private Member member;
    
}
