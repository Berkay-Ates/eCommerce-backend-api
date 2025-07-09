package com.webapp.webapp_api.security.config;

import com.webapp.webapp_api.repository.customer.CustomerRepository;
import com.webapp.webapp_api.security.jwt.JwtAuthFilter;
import com.webapp.webapp_api.security.jwt.JwtTokenService;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS ayarını ekle
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
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Angular frontend adresi
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // Eğer JWT veya Cookie ile auth yapıyorsan bu gerekli

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
