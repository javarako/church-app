package com.javarako.akuc.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.javarako.akuc.model.Offering;

public interface OfferingRepository extends JpaRepository<Offering, Long> {

	Page<Offering> findByOfferingSunday(Date offeringSunday, Pageable pageable);
	Page<Offering> findByOfferingSundayBetween(Date start, Date end, Pageable pageable);
	Page<Offering> findByofferingNumber(Long offeringNumber, Pageable pageable);
	Page<Offering> findByofferingNumberAndOfferingSundayBetween(Long offeringNumber, Date start, Date end, Pageable pageable);
	List<Offering> findByofferingNumberAndOfferingSundayBetween(Integer offeringNumber, Date start, Date end);

}
