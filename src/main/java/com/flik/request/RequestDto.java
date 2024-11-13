package com.flik.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestDto {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("Fields")
    private Fields fields;

//    @JsonProperty("ApplicationData")
//    private ApplicationData applicationData;

    @Getter
    @Setter
    public static class RequestInfo {
        @JsonProperty("SolutionSetName")
        private String solutionSetName;

        @JsonProperty("ExecuteLatestVersion")
        private String executeLatestVersion;
    }

    @Getter
    @Setter
    public static class Fields {
        @JsonProperty("Applicants")
        private Applicants applicants;
        @JsonProperty("ApplicationData")
        private ApplicationData applicationData;

    }

    @Getter
    @Setter
    public static class Applicants {
        @JsonProperty("Applicant")
        private Applicant applicant;
    }

    @Getter
    @Setter
    public static class Applicant {
        @JsonProperty("ApplicantFirstName")
        private String applicantFirstName;

        @JsonProperty("ApplicantMiddleName")
        private String applicantMiddleName;

        @JsonProperty("ApplicantLastName")
        private String applicantLastName;

        @JsonProperty("DateOfBirth")
        private String dateOfBirth;

        @JsonProperty("Gender")
        private String gender;

        @JsonProperty("EmailAddress")
        private String emailAddress;

        @JsonProperty("Identifiers")
        private Identifiers identifiers;

        @JsonProperty("Telephones")
        private Telephones telephones;

        @JsonProperty("Addresses")
        private Addresses addresses;

        @JsonProperty("KeyPersonRelation")
        private String keyPersonRelation;

        @JsonProperty("KeyPersonName")
        private String keyPersonName;

        @JsonProperty("MemberRelationName1")
        private String memberRelationName1;

        @JsonProperty("MemberRelationType1")
        private String memberRelationType1;

        @JsonProperty("MemberRelationName2")
        private String memberRelationName2;

        @JsonProperty("MemberRelationType2")
        private String memberRelationType2;

        @JsonProperty("MemberRelationName3")
        private String memberRelationName3;

        @JsonProperty("MemberRelationType3")
        private String memberRelationType3;

        @JsonProperty("MemberRelationName4")
        private String memberRelationName4;

        @JsonProperty("MemberRelationType4")
        private String memberRelationType4;

        @JsonProperty("NomineeName")
        private String nomineeName;

        @JsonProperty("NomineeRelation")
        private String nomineeRelation;

        @JsonProperty("MemberOtherId1")
        private String memberOtherId1;

        @JsonProperty("MemberOtherId1Type")
        private String memberOtherId1Type;

        @JsonProperty("MemberOtherId2")
        private String memberOtherId2;

        @JsonProperty("MemberOtherId2Type")
        private String memberOtherId2Type;

        @JsonProperty("MemberOtherId3")
        private String memberOtherId3;

        @JsonProperty("MemberOtherId3Type")
        private String memberOtherId3Type;

        @JsonProperty("MemberOtherId4")
        private String memberOtherId4;

        @JsonProperty("MemberOtherId4Type")
        private String memberOtherId4Type;

        @JsonProperty("Accounts")
        private Accounts accounts;

        @JsonProperty("Services")
        private Services services;
    }

    @Getter
    @Setter
    public static class Identifiers {
        @JsonProperty("Identifier")
        private List<Identifier> identifier;
    }

    @Getter
    @Setter
    public static class Identifier {
        @JsonProperty("IdNumber")
        private String idNumber;

        @JsonProperty("IdType")
        private String idType;
    }

    @Getter
    @Setter
    public static class Telephones {
        @JsonProperty("Telephone")
        private Telephone telephone;
    }

    @Getter
    @Setter
    public static class Telephone {
        @JsonProperty("TelephoneNumber")
        private String telephoneNumber;

        @JsonProperty("TelephoneType")
        private String telephoneType;

        @JsonProperty("TelephoneCountryCode")
        private String telephoneCountryCode;
    }

    @Getter
    @Setter
    public static class Addresses {
        @JsonProperty("Address")
        private Address address;
    }

    @Getter
    @Setter
    public static class Address {
        @JsonProperty("AddressType")
        private String addressType;

        @JsonProperty("AddressLine1")
        private String addressLine1;

        @JsonProperty("AddressLine2")
        private String addressLine2;

        @JsonProperty("AddressLine3")
        private String addressLine3;

        @JsonProperty("AddressLine4")
        private String addressLine4;

        @JsonProperty("AddressLine5")
        private String addressLine5;

        @JsonProperty("City")
        private String city;

        @JsonProperty("PinCode")
        private String pinCode;

        @JsonProperty("ResidenceType")
        private String residenceType;

        @JsonProperty("StateCode")
        private String stateCode;
    }

    @Getter
    @Setter
    public static class Accounts {
        @JsonProperty("Account")
        private Account account;
    }

    @Getter
    @Setter
    public static class Account {
        @JsonProperty("AccountNumber")
        private String accountNumber;
    }

    @Getter
    @Setter
    public static class Services {
        @JsonProperty("Service")
        private Service service;
    }
    @Getter
    @Setter
    public static class Services1 {
        @JsonProperty("Service")
        private Service1 service;

        public Services1(Service1 x) {
            this.service= x;
        }
    }
    @Getter
    @Setter
    public static class Service1 {
        @JsonProperty("Id")
        private String id;

        @JsonProperty("Consent")
        private String consent;
        @JsonProperty("EnableSimulation")
        private String enableSimulation;
        @JsonProperty("Skip")
        private String skip;
    }

    @Getter
    @Setter
    public static class Service {
        @JsonProperty("Id")
        private String id;

        @JsonProperty("Operations")
        private Operations operations;
    }

    @Getter
    @Setter
    public static class Operations {
        @JsonProperty("Operation")
        private List<Operation> operation;
    }

    @Getter
    @Setter
    public static class Operation {
        @JsonProperty("Name")
        private String name;

        @JsonProperty("Params")
        private Params params;
    }

    @Getter
    @Setter
    public static class Params {
        @JsonProperty("Param")
        private List<Param> param;
    }

    @Getter
    @Setter
    public static class Param {
        @JsonProperty("Name")
        private String name;

        @JsonProperty("Value")
        private String value;
    }

    @Getter
    @Setter
    public static class ApplicationData {
        @JsonProperty("GSTStateCode")
        private String GSTStateCode;

        @JsonProperty("Services")
        private Services1 services;
    }
}
