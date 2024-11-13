package com.flik.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "credit")
@Data
public class CreditScoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false)

    private Long id;
    private Long customerId;
    private String score;
    private String json;
    private String customerName;
    private String mobileNumber;
    private String address;
    private String panNumber;
    private String documentId;
    private String mfiScore;
    private String plScore;
    private LocalDate createdDate;
    private String status;


}
