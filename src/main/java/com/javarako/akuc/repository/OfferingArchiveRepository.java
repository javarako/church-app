package com.javarako.akuc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.javarako.akuc.entity.OfferingArchive;

public interface OfferingArchiveRepository extends JpaRepository<OfferingArchive, Long> {
	
	@Transactional
	@Modifying
	@Query(value = "CALL offering_archiving(?1, ?2, ?3)", nativeQuery = true)
	public void getOfferingArchiving(Integer offeringNumber, String startDate, String endDate);
}
