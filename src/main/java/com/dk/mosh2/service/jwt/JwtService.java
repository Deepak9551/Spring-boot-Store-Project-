package com.dk.mosh2.service.jwt;

import com.dk.mosh2.config.JwtConfig;
import com.dk.mosh2.model.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Date;

@Component
public class JwtService {

    @Autowired
   private JwtConfig jwtConfig;

    public Jwt generateAccessToken(Users user) {
          // 5m
        return generateToken(user, jwtConfig.getAccessTokenExpiration());

    }
    public Jwt generateRefreshToken(Users user) {

        return generateToken(user, jwtConfig.getRefreshTokenExpiration());

    }

    private Jwt generateToken(Users user, long tokenExpiration) {
        var claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("name", user.getUsername())
                .add("email", user.getEmail())
                .add("roles",user.getRoles())
                .expiration(new Date(System.currentTimeMillis() + (tokenExpiration * 1000 )))
                .issuedAt(new Date())
                .issuer("DK-Universe")
                .build();
        return new Jwt(claims,jwtConfig.getSecretKey());

    }

    // parse the token and get jwt object
public Jwt parseToken(String token){
     try {
         var claims = getClaims(token);
         return new Jwt(claims, jwtConfig.getSecretKey());
     } catch (JwtException e) {
       return null;
     }
}


    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }




}