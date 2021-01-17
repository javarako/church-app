package com.javarako.akuc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/********************************
CREATE TABLE public.reference_code
(
    id bigint NOT NULL,
    type character varying(20) COLLATE pg_catalog."default",
    value character varying(50) COLLATE pg_catalog."default",
    "viewValue" character varying(100) COLLATE pg_catalog."default",
    created_at time with time zone,
    updated_at time with time zone,
    created_by character varying(50) COLLATE pg_catalog."default",
    updated_by character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT reference_code_pkey PRIMARY KEY (id)
)
 *******************************/

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceCode extends AuditModel {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String type;
	private String value;
	private String viewValue;

}
