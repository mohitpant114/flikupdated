package com.flik.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "ready_to_disbursed")
@Data
public class ReadyToDisbursedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false)

    private Long id;
    private LocalDateTime createdDate;
    private Date approvedDate;
    private Long loanId;
    private Long customerId;
    private String partnerLoanId;
    private String partnerName;
    private String productName;//
    private Double loanAmount ;
    private String borrowerName;
    private String accountNumber;//
    private String ifscCode;//
    private String bankName;//
    private String mobileNumber;
    private String disbursedAmount;
    private String processingFee;
    private String gst;
    private String status;

}
