package com.javarako.akuc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javarako.akuc.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
