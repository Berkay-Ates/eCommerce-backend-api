package com.webapp.webapp_api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "password") // password'ü log'lamamak için
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be null.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Surname cannot be null.")
    @Column(nullable = false)
    private String surname;

    @Email(message = "Provide a valid email")
    @NotBlank(message = "Email cannot be null")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password cannot be null")
    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String photoUrl;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal money=BigDecimal.ZERO;

    @Column(nullable = false)
    private boolean isVerified = false;
}
