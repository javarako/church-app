package com.javarako.akuc.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;

import com.javarako.akuc.entity.Expenditure;

import lombok.extern.slf4j.Slf4j;

@DataJpaTest
//@SpringBootTest
@Slf4j
public class ExpenditureRepositoryTest {

	@Autowired
	DataSource dataSource;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	ExpenditureRepository expenditureRepository;

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
			Calendar end = new GregorianCalendar();
			Date endDate = end.getTime();

			Calendar januaryFirst = new GregorianCalendar(end.get(Calendar.YEAR), 0, 1);
			Date beginDate = januaryFirst.getTime();
			
			log.info("/expenditures?committeeCode={}, startDate={}, endDate={}", null,beginDate,endDate);

			Pageable pageable = PageRequest.of(0, 200, Sort.by("requestDate").descending());
			Page<Expenditure> expenditures = expenditureRepository.
							findByRequestDateBetweenOrderByRequestDateDescIdDesc(beginDate, endDate, pageable);
					
			assertThat(expenditures.getContent()).isNotNull();
			assertThat(expenditures.getContent().size()).isGreaterThan(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
