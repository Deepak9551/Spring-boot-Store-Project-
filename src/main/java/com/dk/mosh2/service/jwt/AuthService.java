package com.dk.mosh2.service.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dk.mosh2.exceptions.ResourceNotFoundException;
import com.dk.mosh2.model.Users;
import com.dk.mosh2.repo.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService {

	@Autowired
	private UserRepository userRepository;
	
	public Users currentUser() {
	     // 1 extract current principle ( user details )
	    Authentication authToken = SecurityContextHolder.getContext().getAuthentication();
	    Long userId = (Long) authToken.getPrincipal();
	    log.info("sub: "+userId);

	    // look up for the user
	   return userRepository
	            .findById(Long.valueOf(userId))
	            .orElseThrow(() -> new ResourceNotFoundException("user not found"));
	}
}
