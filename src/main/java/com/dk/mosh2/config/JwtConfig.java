package com.dk.mosh2.config;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@ConfigurationProperties(prefix = "jwt")
@Component
@Data // This generates the Getters and SETTERS needed for binding
public class JwtConfig {

    private String secret;

    private Long accessTokenExpiration;

    private Long refreshTokenExpiration;

    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
@PostConstruct
    public void init(){
        System.out.println("secret : "+secret);
        System.out.println("accessTokenExpiration : "+accessTokenExpiration);
        System.out.println("refreshTokenExpiration : " +refreshTokenExpiration);
    }
}