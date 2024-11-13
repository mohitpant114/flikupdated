package com.flik.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Getter
@Setter
public class LoanRequest {

//    private String date;
private Long id;
    private String customerId;
    private Date  approvedDate;
    private String utr;
    private String umrnNumber;
    private String eSign;

    private String  partnerLoanId;
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
   private Date disbursedDate;
    private Date collectionDate;
    private String bounceCharge;
    private String penalCharge;
    private Double interestPart;
    private String principalPart;
    private String totalPaid;
    private LocalDateTime createdDate;

}
