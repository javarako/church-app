package com.javarako.akuc.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/********************************
CREATE TABLE public.member
(
    id integer NOT NULL,
    first_name character varying COLLATE pg_catalog."default",
    last_name character varying COLLATE pg_catalog."default",
    primary_email character varying COLLATE pg_catalog."default",
    secondary_email character varying COLLATE pg_catalog."default",
    offering_number integer,
    comment text COLLATE pg_catalog."default",
)
 *******************************/

@Getter
@Setter
@ToString
@Entity
public class Member extends AuditModel {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long memberId;

	private String name;
	private String nickName;
	private String primaryEmail;
	private String secondaryEmail;
	@Column(unique = true)
	private Integer offeringNumber;
	private String comment;
	
	@OneToMany(targetEntity = Address.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "member_id")
	private Set<Address> addresses = new HashSet<Address>();

	@OneToMany(targetEntity = Phone.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "member_id")
	private Set<Phone> phones = new HashSet<Phone>();
}