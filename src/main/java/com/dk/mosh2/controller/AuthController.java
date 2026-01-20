package com.dk.mosh2.controller;

import com.dk.mosh2.exceptions.ResourceNotFoundException;
import com.dk.mosh2.dto.JwtDto.JwtResponse;
import com.dk.mosh2.dto.LoginDto;

import com.dk.mosh2.dto.SignInDto;
import com.dk.mosh2.dto.UserDto;
import com.dk.mosh2.enums.Roles;
import com.dk.mosh2.mapper.UserMapper;
import com.dk.mosh2.model.Users;
import com.dk.mosh2.repo.UserRepository;
import com.dk.mosh2.service.jwt.Jwt;
import com.dk.mosh2.service.jwt.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

@Autowired
    private AuthenticationManager authenticationManager;

    public AuthController(UserRepository userRepository ) {
        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
    }

@PostMapping("/login")
    public ResponseEntity<?> loginAuth(@Valid @RequestBody LoginDto loginDto , HttpServletResponse response){

    Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
    if (!authenticate.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 2. Extract Principal safely
    // Ensure your UserDetailsService returns your custom User entity
   UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

    Users user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

    // sending both access or refresh token to the client
    var accessToken =  jwtService.generateAccessToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    // set the refresh token in the cookie to safe auth/refresh
    Cookie cookie = new Cookie("refreshToken",refreshToken.toString());
    cookie.setHttpOnly(true);
    cookie.setMaxAge(604800);
    cookie.setPath("/auth/refresh");

    response.addCookie(cookie);

    JwtResponse responseDto = new JwtResponse();
    responseDto.setToken(accessToken.toString());

    return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> singIn(@Valid @RequestBody SignInDto signInDto ,
                                       UriComponentsBuilder uribuilder){

            var user = userMapper.toEntity(signInDto);
            boolean existsByEmail = userRepository.existsByEmail(signInDto.getEmail());
            if(existsByEmail) {

                return ResponseEntity.badRequest().body(Map.of("error","email is already registered !!"));
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Roles.USER);
            Users registeredUser = userRepository.save(user);
            System.out.println(uribuilder.toUriString());
            var uri =uribuilder.path("/users/{id}").buildAndExpand(registeredUser.getId()).toUri();
            return ResponseEntity.status(201).body(registeredUser).created(uri).build();
}

@PostMapping("/refresh")
public ResponseEntity<?> refresh(@CookieValue(name = "refreshToken") String refreshToken){

        // check token is valid or not
    Jwt refreshTokenJwt = jwtService.parseToken(refreshToken);
    if(refreshTokenJwt.isExpired()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    Long userId = refreshTokenJwt.getUserId();
    Users users = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
    Jwt accessTokenJwt = jwtService.generateAccessToken(users);
    JwtResponse jwtResponse = new JwtResponse();
    jwtResponse.setToken(accessTokenJwt.toString());

    return ResponseEntity.ok(jwtResponse);
}
@GetMapping("/validate")
public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token){
        token = token.replace("Bearer ","");
    Jwt accessTokenJwt = jwtService.parseToken(token);
    if(accessTokenJwt.isExpired()) throw new JwtException("token  is expired !!");
    return ResponseEntity.ok("Token is Valid");
}

@GetMapping("/me")
public ResponseEntity<?> me(){
        // 1 extract current principle ( user details )
    Authentication authToken = SecurityContextHolder.getContext().getAuthentication();
    // Use String.valueOf() instead of (String) cast
    String userId = String.valueOf(authToken.getPrincipal());
    log.info("Extracted userId (sub): " + userId);


    // look up for the user
    Users user = userRepository
            .findById(Long.valueOf(userId))
            .orElseThrow(() -> new ResourceNotFoundException("user not found"));

    UserDto userDto = userMapper.todto(user);

    return ResponseEntity.ok(userDto);
}
    }