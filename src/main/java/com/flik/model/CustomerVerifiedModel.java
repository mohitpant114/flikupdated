package com.flik.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Data
@Getter
@Setter
public class CustomerVerifiedModel {
    private String partnerLoanId;
    private String fintechName;
    private String fintechId;
    private String borrowerName;

    private String mobileNumber;
    private String email;
    private String dob;
    private String gender;
    private String dobJoining;
    private String fatherName;
//    private String maritalStatus;
//    private String spouseName;
//    private String nomineeName;
//    private String nomineeDob;
//    private String nomineeGender;

//    private String referenceName;
//    private String referenceMobileNumber;
//    private String referenceName2;
//    private String referenceMobileNumber2;
//    private String branch;
    private String panNumber;
    private String aadharNumber;
//    private String residentialType;
//    private String area;
    private String pinCode;
    private String city;
    private String state;
    private String address;
    private String longitude;
    private String latitude;
    private String companyName;
//    private String position;
    private String companyAddress;
//    private String professionalEmail;
//    private String companyType;
    private String salary;
    private String empId;
    private String accountName;
    private String accountNumber;
    private String ifscCode;
    private String bankName;
    private String branchName;
    private String bureauDetails;
    private String bureauType;
    private String bureauScore;
    private String newToCredit;
    private String otherDetails;
    private String otherDetails2;
//    private String insuranceCompany;
//    private String coBorrowerName;
//    private String coMobileNumber;
//    private String coEmail;
//    private String coDob;
//    private String coGender;
//    private String coRelation;
//    private String coEmploymentType;
//    private String coCompanyName;
//    private String coPanNumber;
//    private String coAadharNumber;
//    private String coBankName;
//    private String coNameAsPerBank;
//    private String coAccountNumber;
//    private String coIfscCode;
    private LocalDate createdDate;
    private String status;
    private String creditReason;
    private String tvrReason;
    private String operationReason;



}
