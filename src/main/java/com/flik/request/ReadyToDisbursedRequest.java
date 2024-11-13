package com.flik.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class ReadyToDisbursedRequest {
    private Date approvedDate;
    private Long loanId;
    private String  partnerLoanId;
    private String partnerName;
    private String productName;
    private Double loanAmount ;
    private String borrowerId;
    private String borrowerName;
    private String accountNumber;
    private String ifscCode;
    private String bankName;
    private String mobileNumber;
    private String disbursedAmount;

    private String processingFee;
    private String gst;
    private String status;
}
