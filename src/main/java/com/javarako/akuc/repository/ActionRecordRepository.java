package com.javarako.akuc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javarako.akuc.entity.ActionRecord;

public interface ActionRecordRepository extends JpaRepository<ActionRecord, Long> {

}
