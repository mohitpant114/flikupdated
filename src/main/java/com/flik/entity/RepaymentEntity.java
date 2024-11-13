package com.flik.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "repayment")
@Data
public class RepaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false)

    private Long id;

    private Long loanId;
    private String customerId;
    private LocalDate disbursedDate;
     private String partnerLoanId;
    private String partnerName;
    private String branchName;
    private String borrowerName;
    private String mobileNumber;
    private String repaymentAmount;
    private LocalDate dueDate;
    private String collectionDate;
    private String bounceCharge;
    private String penalCharge;
    private String totalPaid;
    private String status;//paid & pending
    private String roiType;
    private String repaymentType;//flat & Reducing
    private String transactionNumber;
    private String modeOfPayment;
    private Double interestPart;
    private Double principalPart;
    private Double outstandingPrincipal;


}
