package com.webapp.webapp_api.controller.Customer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.webapp.webapp_api.dto.customer.CustomerLoginDTO;
import com.webapp.webapp_api.dto.customer.CustomerRegisterDTO;
import com.webapp.webapp_api.model.Customer;
import com.webapp.webapp_api.security.jwt.JwtTokenService;
import com.webapp.webapp_api.service.customer.CustomerService;

@RestController
@RequestMapping("/auth")
public class CustomerAuthController {

    private final CustomerService customerService;
    private final JwtTokenService jwtService;

    public CustomerAuthController(CustomerService customerService, JwtTokenService jwtService) {
        this.customerService = customerService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CustomerRegisterDTO registerDTO) {
        Customer registered = customerService.register(registerDTO);
        String token = jwtService.generateToken(registered.getEmail());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody CustomerLoginDTO loginDTO) {
        Customer customer = customerService.login(loginDTO);
        if (customer == null) return ResponseEntity.status(401).body("Invalid credentials");

        String token = jwtService.generateToken(customer.getEmail());
        return ResponseEntity.ok(token);
    }
}
