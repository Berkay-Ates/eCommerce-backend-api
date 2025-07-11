package com.webapp.webapp_api.dto.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    @NotBlank(message = "Name cannot be null.")
    private String name;

    @NotBlank(message = "Description cannot be null.")
    private String description;

    @NotBlank(message = "PhotoUrl cannot be null.")
    private String photoUrl;

    @NotNull(message = "Price cannot be null.")
    private BigDecimal price;

    @NotNull(message = "Seller ID cannot be null.")
    private Long sellerId;

    @NotNull(message = "Category ID cannot be null.")
    private Long categoryId;

    @NotNull(message = "Stock quantity cannot be null.")
    private Long stockQuantity;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}

