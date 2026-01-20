package com.dk.mosh2.mapper;

import com.dk.mosh2.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dk.mosh2.model.Cart;
import com.dk.mosh2.model.Users;

@Mapper(componentModel = "spring")
public interface UserMapper {
	// mapper method 
//	@Mapping(target = "date" , expression = "java(java.time.LocalDateTime.now())")
	UserDto todto(Users user);
	
	Users toEntity(RegisterUserRequest userRequest);

	Users toEntity(SignInDto userRequest);
	void update(UpdateUserRequest userRequest ,@MappingTarget Users user);
	

}
