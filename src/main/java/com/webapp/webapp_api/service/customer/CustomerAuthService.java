package com.webapp.webapp_api.service.customer;

import java.beans.Transient;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.webapp.webapp_api.dto.customer.CustomerLoginDTO;
import com.webapp.webapp_api.dto.customer.CustomerRegisterDTO;
import com.webapp.webapp_api.model.Customer;
import com.webapp.webapp_api.repository.customer.CustomerRepository;
import com.webapp.webapp_api.service.mail.MailService;

@Service
public class CustomerAuthService {

    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailService mailService;

    public CustomerAuthService(CustomerRepository customerRepository,MailService mailService) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.mailService = mailService;
    }

    @Transient
    public Customer register(CustomerRegisterDTO dto) {
        if (customerRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }

        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPassword(passwordEncoder.encode(dto.getPassword()));
        customer.setSurname(dto.getSurname());

        mailService.sendVerificationEmail(customer.getEmail(),"customer", customer.getEmail());

        return customerRepository.save(customer);
    }

    public Customer login(CustomerLoginDTO dto) {
        Customer customer = customerRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(dto.getPassword(), customer.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return customer; 
    }

    @Transient
    public String verifyEmail(String token) {
        Optional<Customer> customerOpt = customerRepository.findByEmail(token);

        if (customerOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid or expired token.");
        }

        Customer customer = customerOpt.get();
        customer.setVerified(true);
        customerRepository.save(customer);

        return "Customer email successfully verified!";
    }

}
