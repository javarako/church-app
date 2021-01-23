package com.javarako.akuc.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/********************************
CREATE TABLE public.attendance
(
    id bigint NOT NULL,
    date date,
    member_id bigint,
    attendance boolean,
    spouse_attendance boolean,
    created_at timestamp with time zone,
    updated_at timestamp with time zone,
    created_by character varying(50) COLLATE pg_catalog."default",
    updated_by character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT attendance_pkey PRIMARY KEY (id)
)
 *******************************/

@Getter
@Setter
@ToString
@Entity
public class Attendance extends AuditModel {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Date date;
	private Long memberId;
	private boolean attendance;
	private boolean spouseAttendance;
	private String visitorName;

}
