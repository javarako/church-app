package com.javarako.akuc.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javarako.akuc.entity.Member;
import com.javarako.akuc.exception.ApiResponseException;
import com.javarako.akuc.model.JwtResponse;
import com.javarako.akuc.model.LoginRequest;
import com.javarako.akuc.repository.MemberRepository;
import com.javarako.akuc.repository.RoleRepository;
import com.javarako.akuc.service.UserDetailsImpl;
import com.javarako.akuc.util.JwtUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		log.info("Login: {}", loginRequest.getUsername());

		return ResponseEntity.ok(getJwtResponse(loginRequest.getUsername(), loginRequest.getPassword()));
	}

	private JwtResponse getJwtResponse(String userName, String password) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userName, password));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return new JwtResponse(jwt, 
							 userDetails.getId(), 
							 userDetails.getUsername(), 
							 userDetails.getEmail(), 
							 userDetails.getImageName(), 
							 roles);
	}
	
	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPassword(@Valid @RequestBody LoginRequest loginRequest) {
		
		log.info("ResetPassword: {}", loginRequest.getUsername());
		
		if (Strings.isEmpty(loginRequest.getOldPassword())) {
			setNewPassword(loginRequest);
			throw new ApiResponseException("Please login with new password!", HttpStatus.UNAUTHORIZED);
		}
		
		JwtResponse response = getJwtResponse(loginRequest.getUsername(), loginRequest.getOldPassword());
		
		return memberRepository.findByPrimaryEmail(loginRequest.getUsername()).map(existingMember -> {
				existingMember.setPassword(encoder.encode(loginRequest.getPassword()));
				memberRepository.save(existingMember);
				return ResponseEntity.ok(response);
			
		}).orElseThrow(() -> new ApiResponseException(loginRequest.getUsername() + " not found!", HttpStatus.NOT_FOUND));
	}
	
	private Member setNewPassword(final LoginRequest loginRequest) {
		return memberRepository.findByPrimaryEmail(loginRequest.getUsername()).map(existingMember -> {
			if (Strings.isEmpty(existingMember.getPassword())) {
				existingMember.setPassword(encoder.encode(loginRequest.getPassword()));
				return memberRepository.save(existingMember);
			} else {
				throw new ApiResponseException(loginRequest.getUsername() + "'s old password invalid!", HttpStatus.UNAUTHORIZED);
			}

		}).orElseThrow(() -> new ApiResponseException(loginRequest.getUsername() + " not found!", HttpStatus.NOT_FOUND));
	}
}
