package com.flik.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "loan")
@Data
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false)
    private Long id;



    private String customerId;
    @Temporal(TemporalType.TIMESTAMP)  // Assuming it's a timestamp
    private Date approvedDate;
   // private Date approvedDate;
    private String utr;
    private String umrnNumber;
    private String eSign;
    private String partnerLoanId;
    private String partnerName;
    private String branchName;
    private String borrowerName;
    private String mobileNumber;
    private String emailId;
    private String loanType;
    private Double loanAmount;
    private String processingFeeCharge;
    private String processingFee;
    private String gst;
    private String disbursedAmount;
    private Integer tenure ;
    private String numberOfEmi;
    private Integer roiType ;
    private Double roi ;
    private String repayment;

    @Temporal(TemporalType.TIMESTAMP)  // Assuming it's a timestamp
    private Date disbursedDate;

    @Temporal(TemporalType.TIMESTAMP)  // Assuming it's a timestamp
    private Date collectionDate;
//    private Date disbursedDate;
//    private Date collectionDate;
    private String bounceCharge;
    private String penalCharge;
    private Double interestPart;
    private String principalPart;
    private String totalPaid;
    private String status;
    private LocalDateTime createdDate;






}
