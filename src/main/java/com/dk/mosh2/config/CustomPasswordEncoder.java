package com.dk.mosh2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class CustomPasswordEncoder {

 @Bean
 public BCryptPasswordEncoder bCryptPasswordEncoder() {
     return new BCryptPasswordEncoder();
 }
}
