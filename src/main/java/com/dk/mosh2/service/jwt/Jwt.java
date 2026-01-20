package com.dk.mosh2.service.jwt;

import com.dk.mosh2.enums.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

public class Jwt {

    private Claims claims = null;
    private SecretKey secretKey = null;

    public Jwt(Claims claims, SecretKey secretKey) {

        this.claims = claims;
        this.secretKey = secretKey;

    }


    public boolean isValid(String token) {
        return false;
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public Long getUserId() {
        return Long.valueOf(claims.getSubject());
    }

    public Roles getRoles() {
        return Roles.valueOf(claims.get("roles", String.class));
    }

    public boolean isExpired() {
      return claims.getExpiration().before(new Date());
    }


    public String getClaim(String token, String claimName) {
        Claims claims = getClaims(token);
        Object claimValue = claims.get(claimName);
        return claimValue != null ? claimValue.toString() : null;
    }

    public String toString(){
        return Jwts.builder().claims(claims).signWith(secretKey).compact();
    }
}