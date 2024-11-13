package com.flik.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Data
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     private Long userId;
     private String mobileNumber;
    @Column(nullable = false, unique = true, length = 36)
    private String token;
     private LocalDateTime createdTime;
     private LocalDateTime expiryTime;
 }
