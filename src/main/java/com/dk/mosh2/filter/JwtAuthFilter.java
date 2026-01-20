package com.dk.mosh2.filter;

import com.dk.mosh2.service.jwt.Jwt;
import com.dk.mosh2.service.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String authHeader  = request.getHeader("Authorization");

        // if no token forward the request to next filter spring security will take care
        // that to give access to the endpoint (if permit) or not ( if protected )
        // Forward if header is missing or does not start with Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        } else {
            var token = authHeader.substring(7);
            log.info("token : " + token);

            // if token is not valid or expired forward the request to next filter spring security will take care
            // that to give access to the endpoint (if permit) or not ( if protected )
            Jwt jwt = jwtService.parseToken(token);

            if (jwt == null || jwt.isExpired() ) {
                filterChain.doFilter(request, response);

            }

            // if future checks are not execute means
            // token is present and valid

//            Claims claims = jwtService.getClaims(token);
//            System.out.println("claims :"+claims);

            var authtoken =
                    new UsernamePasswordAuthenticationToken(jwt.getUserId()
                            , null,
                            List.of(new SimpleGrantedAuthority("ROLE_"+ jwt.getRoles())));
            authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authtoken);


            filterChain.doFilter(request, response);
        }
    }
}
