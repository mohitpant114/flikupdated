package com.flik.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OverDueRequest {


    private Long loanId;
    private String borrowerId;
    private String borrowerName;
    private String mobileNumber;
    private String disbursedDate;
    private String	dueDate;
    private String dueEmiCount;
    private String overdueAmount;
    private String bounceCharge;
    private String penalCharge;
    private String totalPendingAmount;
    private String status;


}
