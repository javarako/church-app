package com.javarako.akuc.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.javarako.akuc.entity.Offering;
import com.javarako.akuc.entity.OfferingArchive;
import lombok.extern.slf4j.Slf4j;

//@DataJpaTest
@SpringBootTest
@Slf4j
@ActiveProfiles("local")
public class OfferingArchiveRepositoryTest {

	@Autowired
	DataSource dataSource;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	OfferingArchiveRepository offeringArchiveRepository;
	@Autowired
	OfferingRepository offeringRepository;
	
	@Test
	public void di() throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
			DatabaseMetaData metaData = connection.getMetaData();
			log.info(metaData.getURL());
			log.info(metaData.getDriverName());
			log.info(metaData.getUserName());
		}
	}

	//@Test
	public void getOfferingArchivingByIdTest() throws SQLException {
		Long offNum = 108L;
		
		try {
			Pageable pageable = PageRequest.of(0, 20);
			
			Page<Offering> offs = offeringRepository.findByOfferingNumber(offNum, pageable);
			
			offeringArchiveRepository.offeringArchivingById(offNum.intValue());

			List<OfferingArchive> offeringArchives 
				= offeringArchiveRepository.findByOfferingId(offNum.intValue());

			assertThat(offeringArchives).isNotNull();
			assertThat(offs.getSize()).isEqualTo(offeringArchives.size());
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

}
