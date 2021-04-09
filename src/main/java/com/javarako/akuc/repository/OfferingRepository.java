package com.javarako.akuc.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.javarako.akuc.entity.Offering;

public interface OfferingRepository extends JpaRepository<Offering, Long> {

	List<Offering> findByOfferingSunday(Date offeringSunday);
	//Page<Offering> findByOfferingSunday(Date offeringSunday, Pageable pageable);
	List<Offering> findByOfferingSundayBetweenOrderByOfferingSundayAscOfferingNumberAsc(Date start, Date end);
	Page<Offering> findByOfferingSundayBetween(Date start, Date end, Pageable pageable);
	Page<Offering> findByOfferingNumber(Long offeringNumber, Pageable pageable);
	Page<Offering> findByOfferingNumberAndOfferingSundayBetween(Long offeringNumber, Date start, Date end, Pageable pageable);
	List<Offering> findByOfferingNumberAndOfferingSundayBetween(Integer offeringNumber, Date start, Date end);

}
