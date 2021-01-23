package com.javarako.akuc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javarako.akuc.entity.ReferenceCode;

public interface ReferenceCodeRepository extends JpaRepository<ReferenceCode, Long> {

	List<ReferenceCode> findByType(String type);

}
