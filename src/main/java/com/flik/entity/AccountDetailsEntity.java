package com.flik.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "bank_details")
public class AccountDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 15, nullable = false)
    private Long id;

    @Column(name = "customer_id", length = 15)
    private Long customerId;
    @Column(name = "ifsc_code", length = 20)
    private String ifscCode;
    @Column(name = "bank_name", length = 55)
    private String bankName;
    @Column(name = "branch_name", length = 55)
    private String branchName;
    @Column(name = "account_number", length = 55)
    private String accountNumber;
    @Column(name = "name_on_bank", length = 55)
    private String nameOnBank;
    @Column(name = "created_date", length = 35)
    private LocalDateTime createdDate;
    private String status;

}
