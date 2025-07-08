package com.webapp.webapp_api.controller.Seller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.webapp_api.dto.seller.SellerLoginDTO;
import com.webapp.webapp_api.dto.seller.SellerRegisterDTO;
import com.webapp.webapp_api.model.Seller;
import com.webapp.webapp_api.security.jwt.JwtTokenService;
import com.webapp.webapp_api.service.seller.SellerAuthService;

@RestController
@RequestMapping("/auth/seller")
public class SellerAuthController {

    private final SellerAuthService sellerService;
    private final JwtTokenService jwtService;

    public SellerAuthController(SellerAuthService sellerService, JwtTokenService jwtService) {
        this.sellerService = sellerService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody SellerRegisterDTO registerDTO) {       
        Seller registeredSeller = sellerService.register(registerDTO);
        String jwtToken = jwtService.generateToken(registeredSeller.getEmail());       
        Map<String, String> response = Map.of("token", jwtToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody SellerLoginDTO loginDTO) {
        Seller seller = sellerService.login(loginDTO);
        if (seller == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        if (!seller.isVerified()) {
            return ResponseEntity.status(403).body(Map.of("error", "The account is unverified"));
        }

        String token = jwtService.generateToken(seller.getEmail());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        try {
            String message = sellerService.verifyEmail(token);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
