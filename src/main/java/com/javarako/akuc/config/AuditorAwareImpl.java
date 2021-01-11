package com.javarako.akuc.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {
	 
    public Optional<String> getCurrentAuditor() {
    	try {
            String user = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            log.info("Logined user: {}", user);
            return Optional.ofNullable(user);
			
		} catch (Exception e) {
			log.warn(e.getMessage());
			return Optional.ofNullable("anonymous");
		}
    }
}
