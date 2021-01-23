package com.javarako.akuc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javarako.akuc.entity.Phone;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
	
}
