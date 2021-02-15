package com.javarako.akuc.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javarako.akuc.entity.DepositDetail;

public interface DepositDetailRepository extends JpaRepository<DepositDetail, Date> {

}
