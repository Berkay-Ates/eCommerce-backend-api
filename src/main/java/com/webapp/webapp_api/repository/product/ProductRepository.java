package com.webapp.webapp_api.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webapp.webapp_api.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
