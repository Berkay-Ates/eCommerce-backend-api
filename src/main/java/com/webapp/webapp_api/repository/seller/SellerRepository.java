package com.webapp.webapp_api.repository.seller;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webapp.webapp_api.model.Seller;


public interface SellerRepository extends JpaRepository<Seller,Long>{
    Optional<Seller> findByEmail(String email);
    boolean existsByEmail(String email);   
}