package com.flik.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CibilModel {

    private String applicantFirstName;
    private String applicantMiddleName;
    private String applicantLastName;
    private String dateOfBirth;
    private String gender;
    private String idNumber;
    private String telephoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String pinCode;
    private String stateCode;
}
