package com.javarako.akuc.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.javarako.akuc.entity.AccountCode;

public interface AccountCodeRepository extends JpaRepository<AccountCode, String> {

	List<AccountCode> findByCommitteeCode(String committeeCode);

}
