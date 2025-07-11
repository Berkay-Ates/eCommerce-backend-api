package com.webapp.webapp_api.dto.category;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.webapp.webapp_api.utils.ProductCategory;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @CreationTimestamp
    private LocalDateTime createdAt;
}


