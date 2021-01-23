package com.javarako.akuc.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;

import com.javarako.akuc.entity.Address;
import com.javarako.akuc.entity.Member;
import com.javarako.akuc.entity.Phone;
import com.javarako.akuc.util.AddressType;
import com.javarako.akuc.util.PhoneType;

@DataJpaTest
//@SpringBootTest 
public class MemberRepositoryTest {

	@Autowired
	DataSource dataSource;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	PhoneRepository phoneRepository;

	@Test
	public void di() throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
			DatabaseMetaData metaData = connection.getMetaData();
			System.out.println(metaData.getURL());
			System.out.println(metaData.getDriverName());
			System.out.println(metaData.getUserName());
		}
	}

	@Test
	public void memberSave() throws SQLException {

		Member member = new Member();
		member.setName("name");
		member.setNickName("nick");
		member.setOfferingNumber(1);
		
		Member newMember = memberRepository.save(member);

		Phone phone = new Phone();
		phone.setType(PhoneType.Home);
		phone.setNumber("111");
		phone.setMember(newMember);
		
		phoneRepository.save(phone);
		
		assertThat(newMember).isNotNull();

		Optional<Member> existingMember = memberRepository.findById(newMember.getMemberId());
		assertThat(existingMember.get()).isNotNull();

		Page<Member> nonExistingMember = memberRepository.findByNameContainsIgnoreCaseOrNickNameContainsIgnoreCaseOrSpouseNameContainsIgnoreCaseOrSpouseNickNameContainsIgnoreCase("superman", "superman", "superman", "superman", null);
		assertThat(nonExistingMember.getTotalElements()).isEqualTo(0);
	}

	@Test
	public void memberSearchByName() throws SQLException {
		Member member = new Member();
		member.setName("name");
		member.setNickName("nick");
		
		Member newMember = memberRepository.save(member);
		assertThat(newMember).isNotNull();
		
		Page<Member> existingMember_name = memberRepository.findByNameContainsIgnoreCaseOrNickNameContainsIgnoreCaseOrSpouseNameContainsIgnoreCaseOrSpouseNickNameContainsIgnoreCase("nam", "nam", "nam", "nam", null);
		assertThat(existingMember_name.getTotalElements()).isEqualTo(1);
	}
	
	@Test
	public void memberSearchByNickName() throws SQLException {
		Member member = new Member();
		member.setName("name");
		member.setNickName("nick");
		
		Member newMember = memberRepository.save(member);
		assertThat(newMember).isNotNull();
		
		Page<Member> existingMember_nick = memberRepository.findByNameContainsIgnoreCaseOrNickNameContainsIgnoreCaseOrSpouseNameContainsIgnoreCaseOrSpouseNickNameContainsIgnoreCase("nic", "nic", "nic", "nic", null);
		assertThat(existingMember_nick.getTotalElements()).isEqualTo(1);
	}

	@Test
	public void memberSaveWithPhone() throws SQLException {

		Phone phone = new Phone();
		phone.setType(PhoneType.Home);
		phone.setNumber("111");

		Phone phone2 = new Phone();
		phone2.setType(PhoneType.Work);
		phone2.setNumber("222");

//		Address address = Address.builder()
//				.city("aurora")
//				.type(AddressType.Home)
//				.build();
		
		Address address = new Address();
		address.setCity("aurora");
		address.setType(AddressType.Home);
		
		Member member = new Member();
		member.setName("name");
		member.getPhones().add(phone);
		member.getPhones().add(phone2);
		member.getAddresses().add(address);
		
		
		
		Member newMember = memberRepository.save(member);

		assertThat(newMember).isNotNull();

		Optional<Member> existingMember = memberRepository.findById(newMember.getMemberId());
		assertThat(existingMember.get()).isNotNull();

		Page<Member> nonExistingMember = memberRepository.findByNameContainsIgnoreCaseOrNickNameContainsIgnoreCaseOrSpouseNameContainsIgnoreCaseOrSpouseNickNameContainsIgnoreCase("superman", "superman", "superman", "superman", null);
		assertThat(nonExistingMember.getTotalElements()).isEqualTo(0);	
	}

}
