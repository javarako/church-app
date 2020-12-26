package com.javarako.akuc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.javarako.akuc.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Page<Member> findByNameContainsIgnoreCaseOrNickNameContainsIgnoreCase(
			String name, String nickName, Pageable pageable);

	Member findByOfferingNumber(Integer offeringNumber);
}
