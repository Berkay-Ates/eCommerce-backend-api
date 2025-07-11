package com.webapp.webapp_api.service.seller;

import java.beans.Transient;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.webapp.webapp_api.dto.seller.SellerLoginDTO;
import com.webapp.webapp_api.dto.seller.SellerRegisterDTO;
import com.webapp.webapp_api.model.Seller;
import com.webapp.webapp_api.repository.seller.SellerRepository;
import com.webapp.webapp_api.service.mail.MailService;

@Service
public class SellerAuthService {

    private final SellerRepository sellerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailService mailService;

    public SellerAuthService(SellerRepository sellerRepository,MailService mailService) {
        this.sellerRepository = sellerRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.mailService = mailService;
    }

    @Transient
    public Seller register(SellerRegisterDTO dto) {
        if (sellerRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }

        Seller seller = new Seller();
        seller.setName(dto.getName());
        seller.setEmail(dto.getEmail());
        seller.setPassword(passwordEncoder.encode(dto.getPassword()));

        mailService.sendVerificationEmail(seller.getEmail(),"seller", seller.getEmail());

        return sellerRepository.save(seller);
    }

    public Seller login(SellerLoginDTO dto) {
        Seller seller = sellerRepository.findByEmail(dto.getEmail())
            .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(dto.getPassword(), seller.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return seller; 
    }

    @Transient
    public String verifyEmail(String token) {
        Optional<Seller> sellerOpt = sellerRepository.findByEmail(token);

        if (sellerOpt.isEmpty()) throw new IllegalArgumentException("Invalid or expired token.");

        Seller seller = sellerOpt.get();
        seller.setVerified(true);
        sellerRepository.save(seller);

        return "Seller email successfully verified!";
    }
    
}
