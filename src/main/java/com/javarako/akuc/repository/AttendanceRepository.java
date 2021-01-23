package com.javarako.akuc.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javarako.akuc.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	List<Attendance> findByDate(Date date);

	Optional<Attendance> findByMemberIdAndDate(Long memberId, Date date);
	
}
