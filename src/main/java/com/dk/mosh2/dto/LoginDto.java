package com.dk.mosh2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginDto {

    @Email
    @NotBlank(message = "email must be required")
    private String email;
    @NotBlank(message = "password must be required")
    @Size(min = 4,max = 12, message = "password must be between 4 to 12 characters")
    private String password;
}
