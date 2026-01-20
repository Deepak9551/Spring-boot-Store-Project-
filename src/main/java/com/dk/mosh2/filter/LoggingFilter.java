package com.dk.mosh2.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // uri means  the part after domain name
        System.out.println("request uri " +request.getRequestURI());
        // full url means including domain name
        System.out.println("request " + request.getRequestURL());

       filterChain.doFilter(request,response);

        System.out.println("response status : "+response.getStatus());
    }
}
