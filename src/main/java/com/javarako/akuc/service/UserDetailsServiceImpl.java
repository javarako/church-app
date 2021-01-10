package com.javarako.akuc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javarako.akuc.model.Member;
import com.javarako.akuc.repository.MemberRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	MemberRepository repository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member user = repository.findByPrimaryEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with primaryEmail: " + username));

		return UserDetailsImpl.build(user);
	}

}
