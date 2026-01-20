package com.dk.mosh2.dto;


import com.dk.mosh2.validation.LowerCase;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RegisterUserRequest {
	@NotEmpty(message = "username must is required")
	@Size(max = 255 , message = "username must be less than 255")
	@LowerCase
	private String username;
	
	@NotEmpty
	@Size(min=6,max = 12 ,message = "password must be between 6 to 12 characters")
	private String password;
	@Email(message = "email must be valid")
	@NotEmpty(message="email must be required")
	private String email;
}
