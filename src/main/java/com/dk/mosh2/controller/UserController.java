 package com.dk.mosh2.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import com.dk.mosh2.enums.Roles;
import com.dk.mosh2.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.dk.mosh2.dto.RegisterUserRequest;
import com.dk.mosh2.dto.UpdateUserRequest;
import com.dk.mosh2.dto.UserDto;
import com.dk.mosh2.dto.changePasswordRequest;
import com.dk.mosh2.mapper.UserMapper;

import com.dk.mosh2.repo.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/users")
public class UserController {

	
	private UserRepository userRepository;
	
	@Autowired
	private UserMapper userMapper;

	public UserController(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	
//	@GetMapping("/all")
//	public ResponseEntity<List<UserDto>> getAllUsers() {
//		ResponseEntity<User> resonse = null;
//		try {
//			
//			List<User> users = userRepository.findAll();
//			// here we use stream api to return userDTO
//			List<UserDto> usersDto = users.stream()
//			.map(userMapper::todto).toList();
//			if(users != null) return ResponseEntity.ok(usersDto);
//			else return ResponseEntity.internalServerError().build();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			return ResponseEntity.badRequest().build();
//		}
//	}
	@GetMapping("/all")
	public ResponseEntity<?> getAllUsers(
//			@RequestHeader(name = "x-auth")  String authToken,
			@RequestParam(required = false , defaultValue = "username") String sort ) {
		ResponseEntity<Users> resonse = null;
		try {
//			System.out.println(authToken);
			boolean allowed = Set.of("username","email").contains(sort);
			if(!allowed) {
				return ResponseEntity.status(400).body("Required Sort field are [username , email] !!");
			}
			List<Users> users = userRepository.findAll(Sort.by(sort));
			// here we use stream api to return userDTO
			List<UserDto> usersDto = users.stream()
			.map(userMapper::todto).toList();
			if(users != null) return ResponseEntity.ok(usersDto);
			else return ResponseEntity.internalServerError().build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> registerUser(
			@Valid @RequestBody RegisterUserRequest userRequest,
			UriComponentsBuilder uribuilder
			){
		var user = userMapper.toEntity(userRequest);
		boolean existsByEmail = userRepository.existsByEmail(userRequest.getEmail());
		if(existsByEmail) {
			
			return ResponseEntity.badRequest().body(Map.of("error","email is already registered !!"));
		}
		user.setRoles(Roles.USER);
		Users registeredUser = userRepository.save(user);
		System.out.println(uribuilder.toUriString());
		var uri =uribuilder.path("/users/{id}").buildAndExpand(registeredUser.getId()).toUri();
		return ResponseEntity.status(201).body(registeredUser).created(uri).build();
	}
	
	@PatchMapping("/update/{id}")
	public ResponseEntity<?> updateUser(
			@RequestBody UpdateUserRequest userRequest ,
			@PathVariable Long id
			){
		Users user = userRepository.findById(id).orElse(null);
		if(user == null) return ResponseEntity.notFound().build();
		userMapper.update(userRequest, user);
		userRepository.save(user);
		return ResponseEntity.ok("user data update successfully");
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteUser(
	
			@PathVariable Long id
			){
		Users user = userRepository.findById(id).orElse(null);
		if(user == null) return ResponseEntity.notFound().build();
	
		userRepository.delete(user);
		return ResponseEntity.ok("user data deleted successfully");
	}
	
	@PostMapping("/changepassword/{id}")
	public ResponseEntity<?> updatePassword(
			@PathVariable Long id
			,
			@RequestBody changePasswordRequest changePasswordRequest
			){
		
		Users user = userRepository.findById(id).orElse(null);
		if(user == null) return ResponseEntity.notFound().build();
	
		if(!user.getPassword().equals(changePasswordRequest.getOldpassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("password not matched");
		}
		user.setPassword(changePasswordRequest.getNewpassword());
		userRepository.save(user);
		return ResponseEntity.ok("password updated successfully");
	}
	
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<?> validationExceptionHandler(MethodArgumentNotValidException ex){
//		var errorMap  = new HashMap<>(); 
//		ex.getBindingResult().getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
//		return ResponseEntity.badRequest().body(errorMap);
//	}
	
}

