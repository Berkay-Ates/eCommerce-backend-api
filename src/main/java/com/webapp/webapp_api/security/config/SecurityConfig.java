package com.webapp.webapp_api.security.config;

import com.webapp.webapp_api.repository.customer.CustomerRepository;
import com.webapp.webapp_api.security.jwt.JwtAuthFilter;
import com.webapp.webapp_api.security.jwt.JwtTokenService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtTokenService jwtService;
    private final CustomerRepository customerRepository;

    public SecurityConfig(JwtTokenService jwtService, CustomerRepository customerRepository) {
        this.jwtService = jwtService;
        this.customerRepository = customerRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", 
                            "/auth/customer/**",
                            "/auth/seller/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtAuthFilter(jwtService, customerRepository),
                             UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
