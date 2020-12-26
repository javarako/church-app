package com.javarako.akuc.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.javarako.akuc.util.AddressType;
import com.javarako.akuc.util.CountryType;
import com.javarako.akuc.util.ProvinceType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/********************************
CREATE TABLE public.address
(
    id bigint NOT NULL,
    member_id bigint NOT NULL,
    type character(5) COLLATE pg_catalog."default",
    address_1 character varying COLLATE pg_catalog."default",
    address_2 character varying COLLATE pg_catalog."default",
    city character(20) COLLATE pg_catalog."default",
    province character(20) COLLATE pg_catalog."default",
    country character(20) COLLATE pg_catalog."default",
    postal_code character(6) COLLATE pg_catalog."default",
    main_address boolean,
)
 *******************************/

@Getter
@Setter
@ToString
@Entity
public class Address extends AuditModel {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Enumerated(EnumType.STRING)
	private AddressType type;
	private String address1;
	private String address2;
	private String city;
	@Enumerated(EnumType.STRING)
	private ProvinceType province;
	@Enumerated(EnumType.STRING)
	private CountryType country;
	private String postalCode;
	private Boolean mailingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
	private Member member;

}
