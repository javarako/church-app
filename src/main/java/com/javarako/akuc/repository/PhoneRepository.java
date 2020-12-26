package com.javarako.akuc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javarako.akuc.model.Phone;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
	
}
