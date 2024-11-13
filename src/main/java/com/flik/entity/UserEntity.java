package com.flik.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data

public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false)
    private Long id;
    private String fintechId;
    private String fintechName;
    private String partnerAccountNumber;
    private String ifscCode;
    private String bankName;
    private String processingFeePart;
    private String commision;
    private String pan;
    private String cinNumber;
    private String gstNumber;
    private String directorName;
    private String directorPan;
    private String directorAadhar;
    private String spocName;
    private String spocMobileNumber;
    private String emailId;
    private String createPassword;
    private String confirmPassword;
    private String status;


}
