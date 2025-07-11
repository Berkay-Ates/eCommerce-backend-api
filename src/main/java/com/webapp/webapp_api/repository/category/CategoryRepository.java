package com.webapp.webapp_api.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webapp.webapp_api.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}
