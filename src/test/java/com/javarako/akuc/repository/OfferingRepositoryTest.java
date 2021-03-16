package com.javarako.akuc.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;

import com.javarako.akuc.entity.Offering;
import com.javarako.akuc.util.Utils;

import lombok.extern.slf4j.Slf4j;

@DataJpaTest
//@SpringBootTest
@Slf4j
public class OfferingRepositoryTest {

	@Autowired
	DataSource dataSource;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	OfferingRepository offeringRepository;

	@Test
	public void di() throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
			DatabaseMetaData metaData = connection.getMetaData();
			System.out.println(metaData.getURL());
			System.out.println(metaData.getDriverName());
			System.out.println(metaData.getUserName());
		}
	}

	//@Test
	public void searchByDate() throws SQLException {
		
		try {
			Date offeringSunday = Utils.getSundayFromToday();
			log.info("/offerings?offeringSunday={}", offeringSunday);

			Pageable pageable = PageRequest.of(0, 100, Sort.by("id").descending());
			Page<Offering> offerings = offeringRepository.findByOfferingSunday(offeringSunday, pageable);
			
			/***
			Map<String, Object> response = new HashMap<>();
			response.put("offerings", offerings.getContent());
			response.put("currentPage", offerings.getNumber());
			response.put("totalItems", offerings.getTotalElements());
			response.put("totalPages", offerings.getTotalPages());
			***/
			
			assertThat(offerings.getContent()).isNotNull();
			assertThat(offerings.getContent().size()).isGreaterThan(0);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

}
