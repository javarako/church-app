package com.javarako.akuc.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.javarako.akuc.util.RoleType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/********************************
CREATE TABLE public.role
(
    id bigint NOT NULL,
    member_id bigint NOT NULL,
    type character varying(20) COLLATE pg_catalog."default",
    created_at time with time zone,
    updated_at time with time zone,
    created_by character varying(50) COLLATE pg_catalog."default",
    updated_by character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT role_pkey PRIMARY KEY (id)
        USING INDEX TABLESPACE akuc_space,
    CONSTRAINT role_fk FOREIGN KEY (member_id)
        REFERENCES public.member (member_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
 *******************************/

@Getter
@Setter
@ToString
@Entity
public class Role extends AuditModel {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Enumerated(EnumType.STRING)
	private RoleType type;
	
    @ManyToOne(fetch = FetchType.LAZY)
	private Member member;
    
}