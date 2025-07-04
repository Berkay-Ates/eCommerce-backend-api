package com.webapp.webapp_api.service.customer;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.webapp.webapp_api.dto.customer.CustomerLoginDTO;
import com.webapp.webapp_api.dto.customer.CustomerRegisterDTO;
import com.webapp.webapp_api.model.Customer;
import com.webapp.webapp_api.repository.customer.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); 
    }

    public Customer register(CustomerRegisterDTO dto) {
        if (customerRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }

        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPassword(passwordEncoder.encode(dto.getPassword()));
        customer.setSurname(dto.getSurname());
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
}
