package com.javarako.akuc.entity;

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
