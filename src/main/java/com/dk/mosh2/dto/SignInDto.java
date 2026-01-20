package com.dk.mosh2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInDto {

@NotBlank
    private String username;

    @NotBlank
    private String password;
    @NotBlank
    @Email
    private String email;
}
