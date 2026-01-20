package com.dk.mosh2.config;

import com.dk.mosh2.enums.Roles;
import com.dk.mosh2.filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //state less session management  ( token based authentication )
       return http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req->{
                    req.requestMatchers("/carts/**").permitAll()
                            .requestMatchers("/auth/login").permitAll()
                            .requestMatchers("/auth/signIn").permitAll()
                            .requestMatchers("/auth/refresh").permitAll()
                            .requestMatchers("/admin/**").hasRole(Roles.ADMIN.name())
                            .requestMatchers("/checkout/webhook").permitAll()
                            .requestMatchers(HttpMethod.POST,"/users").permitAll()
                            .anyRequest().authenticated();
                })

               .formLogin(Customizer.withDefaults())
               .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
               .exceptionHandling(

                       e->
             {
              e.authenticationEntryPoint
              (new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

              e.accessDeniedHandler
              ((request,response
                              ,exception)->
              response.setStatus(HttpStatus.FORBIDDEN.value())
                                   );

                       }
               )
               .build();
    }


}
