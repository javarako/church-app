package com.javarako.akuc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javarako.akuc.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
