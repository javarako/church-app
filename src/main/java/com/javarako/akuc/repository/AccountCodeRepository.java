package com.javarako.akuc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javarako.akuc.model.AccountCode;

public interface AccountCodeRepository extends JpaRepository<AccountCode, String> {

}
