package com.javarako.akuc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javarako.akuc.model.Role;
import com.javarako.akuc.util.RoleType;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findByType(RoleType type);
	
}
