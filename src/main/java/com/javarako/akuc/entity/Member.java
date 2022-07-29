package com.javarako.akuc.entity;

import java.util.Date;
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
	private String craName;
	private String spouseName;
	private String spouseNickName;
	private String password;
	@Column(unique = true)
	private String primaryEmail;
	private String secondaryEmail;
	@Column(unique = true)
	private Integer offeringNumber;
	private String comment;
	private String locationCode;
	private String groupCode;
	private Date birthDate;
	private Date spouseBirthDate;
	private String imageName;
	
	@OneToMany(targetEntity = Address.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "member_id")
	private Set<Address> addresses = new HashSet<Address>();

	@OneToMany(targetEntity = Phone.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "member_id")
	private Set<Phone> phones = new HashSet<Phone>();
	
	@OneToMany(targetEntity = Role.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "member_id")
	private Set<Role> roles = new HashSet<Role>();

}