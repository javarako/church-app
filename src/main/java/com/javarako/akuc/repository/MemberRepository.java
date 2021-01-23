package com.javarako.akuc.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.javarako.akuc.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Page<Member> findByNameContainsIgnoreCaseOrNickNameContainsIgnoreCaseOrSpouseNameContainsIgnoreCaseOrSpouseNickNameContainsIgnoreCase(
			String name, String nickName, String spouseName, String spouseNickName, Pageable pageable);

	Member findByOfferingNumber(Integer offeringNumber);
	
	Boolean existsByPrimaryEmail(String primaryEmail);
	
	Optional<Member> findByPrimaryEmail(String primaryEmail);
}
