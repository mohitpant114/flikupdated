package com.flik.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class RepaymentRequest {
    private Long loanId;
    private String partnerLoanId;
    private String partnerName;
    private String branchName;
    private String borrowerName;
    private String mobileNumber;
    private String repayment;
    private LocalDate dueDate;
    private String collectionDate;
    private String bounceCharge;
    private String penalCharge;
    private String totalPaid;
    private String status;
    private String transactionNumber;
    private String modeOfPayment;



}
