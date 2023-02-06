package com.javarako.akuc.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import com.javarako.akuc.entity.OfferingArchive;

public interface OfferingArchiveRepository extends JpaRepository<OfferingArchive, Long> {
	
	/***
	@Transactional
	@Modifying
	@Query(value = "CALL offeringArchiving(?1, ?2, ?3)", nativeQuery = true)
	public void getOfferingArchiving(Integer offeringNumber, Date startDate, Date endDate);
	
	@Transactional
	@Modifying
	@Query(value = "CALL offeringArchivingId(?1)", nativeQuery = true)
	public void getOfferingArchivingById(Integer offeringNumber);
	***/
	
	@Procedure
	public void offeringArchiving(Integer offeringNumber, Date startDate, Date endDate);
	
	@Procedure
	public void offeringArchivingById(Integer offeringNumber);
	
	List<OfferingArchive> findByOfferingId(Integer offeringId);
}
