package com.webapp.webapp_api.controller.Customer;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.webapp.webapp_api.dto.customer.CustomerLoginDTO;
import com.webapp.webapp_api.dto.customer.CustomerRegisterDTO;
import com.webapp.webapp_api.model.Customer;
import com.webapp.webapp_api.security.jwt.JwtTokenService;
import com.webapp.webapp_api.service.customer.CustomerAuthService;

@RestController
@RequestMapping("/auth/customer")
public class CustomerAuthController {

    private final CustomerAuthService customerService;
    private final JwtTokenService jwtService;

    public CustomerAuthController(CustomerAuthService customerService, JwtTokenService jwtService) {
        this.customerService = customerService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody CustomerRegisterDTO registerDTO) {
        Customer registered = customerService.register(registerDTO);
        String jwtToken = jwtService.generateToken(registered.getEmail());
        Map<String, String> response = Map.of("token", jwtToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody CustomerLoginDTO loginDTO) {
        Customer customer = customerService.login(loginDTO);
        if (customer == null) return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));

        if (!customer.isVerified()) return ResponseEntity.status(403).body(Map.of("error", "The account is unverified"));

        String token = jwtService.generateToken(customer.getEmail());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        try {
            String message = customerService.verifyEmail(token);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
