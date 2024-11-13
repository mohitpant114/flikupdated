package com.flik.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Getter
@Setter
public class CustomerModel {
    private Long id;
    private String partnerLoanId;
    private String fintechName;
    private String borrowerName;
    private String mobileNumber;
    private String email;
    private String dob;
    private String gender;
    private String dobJoining;
    private String fatherName;
    //    private String spouseName;
//    private String nomineeName;
//    private String nomineeDob;
//    private String nomineeGender;
//    private String referenceName;
//    private String referenceMobileNumber;
//    private String referenceName2;
//    private String referenceMobileNumber2;
//    private String branch;
    private String aadharNumber;
    private String panNumber;
    private String accountName;
    private String accountNumber;
    private String ifscCode;
    private String bankName;
    //    private String city;
//    private String state;
//    private String address;
//    private String longitude;
//    private String latitude;
    private String companyName;
    private String position;
    private String address;
    private String state;
    private String city;
    private String pinCode;
    private String salary;
    private String empId;
    private String companyAddress;
    private String professionalEmail;
    private String companyType;
    //    private String salary;
//    private String accountName;
//    private String accountNumber;
//    private String ifscCode;
//    private String bankName;
//    private String branchName;
    private String bureauDetails;
    private String bureauType;
    private String bureauScore;
    private String newToCredit;
    private String otherDetails;
    private String otherDetails2;
    //    private String insuranceCompany;
//    private String dobJoining;
//    private String fatherName;
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

    private Double loanAmount;
    private String numberOfEmi;
    private String emiAmount; // Holds the "repayment" from LoanEntity as per your note
    private Double principalPart;
    private String processingFee;
    private String tenure;
    private Double totalPaid;
    private String DisbursedAmount;
    private LocalDateTime ApprovedDate;
    private LocalDateTime DisbursedDate;
    private LocalDateTime CollectionDate;

}
