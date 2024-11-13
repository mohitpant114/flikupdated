package com.flik.respons;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApiResponseDto {
    @JsonProperty("Status")
    private String Status;

    @JsonProperty("ResponseInfo")
    private ResponseInfo ResponseInfo;

    @JsonProperty("Fields")
    private Fields Fields;

    @JsonProperty("ApplicationData")
    private ApplicationData ApplicationData;


    @lombok.Data
    public static class ResponseInfo {
        @JsonProperty("ApplicationId")
        private long ApplicationId;

        @JsonProperty("SolutionSetInstanceId")
        private String SolutionSetInstanceId;
    }

    @lombok.Data
    public static class Fields {
        @JsonProperty("Applicants")
        private Applicants Applicants;

        @JsonProperty("ApplicationData")
        private ApplicationData ApplicationData;

        @JsonProperty("Decision")
        private String Decision;

        @JsonProperty("ApplicationId")
        private String ApplicationId;
    }

    @lombok.Data
    public static class Applicants {
        @JsonProperty("Applicant")
        private Applicant Applicant;
    }

    @lombok.Data
    public static class Applicant {
        @JsonProperty("ApplicantFirstName")
        private String ApplicantFirstName;

        @JsonProperty("ApplicantMiddleName")
        private String ApplicantMiddleName;

        @JsonProperty("ApplicantLastName")
        private String ApplicantLastName;

        @JsonProperty("DateOfBirth")
        private String DateOfBirth;

        @JsonProperty("Gender")
        private String Gender;

        @JsonProperty("EmailAddress")
        private String EmailAddress;

        @JsonProperty("Identifiers")
        private Identifiers Identifiers;

        @JsonProperty("Telephones")
        private Telephones Telephones;

        @JsonProperty("Addresses")
        private Addresses Addresses;

        @JsonProperty("KeyPersonRelation")
        private String KeyPersonRelation;

        @JsonProperty("KeyPersonName")
        private String KeyPersonName;

        @JsonProperty("MemberRelationName1")
        private String MemberRelationName1;

        @JsonProperty("MemberRelationType1")
        private String MemberRelationType1;

        @JsonProperty("MemberRelationName2")
        private String MemberRelationName2;

        @JsonProperty("MemberRelationType2")
        private String MemberRelationType2;

        @JsonProperty("MemberRelationName3")
        private String MemberRelationName3;

        @JsonProperty("MemberRelationType3")
        private String MemberRelationType3;

        @JsonProperty("MemberRelationName4")
        private String MemberRelationName4;

        @JsonProperty("MemberRelationType4")
        private String MemberRelationType4;

        @JsonProperty("NomineeName")
        private String NomineeName;

        @JsonProperty("NomineeRelation")
        private String NomineeRelation;

        @JsonProperty("MemberOtherId1")
        private String MemberOtherId1;

        @JsonProperty("MemberOtherId1Type")
        private String MemberOtherId1Type;

        @JsonProperty("MemberOtherId2")
        private String MemberOtherId2;

        @JsonProperty("MemberOtherId2Type")
        private String MemberOtherId2Type;

        @JsonProperty("MemberOtherId3")
        private String MemberOtherId3;

        @JsonProperty("MemberOtherId3Type")
        private String MemberOtherId3Type;

        @JsonProperty("MemberOtherId4")
        private String MemberOtherId4;

        @JsonProperty("MemberOtherId4Type")
        private String MemberOtherId4Type;

        @JsonProperty("Accounts")
        private Accounts Accounts;

        @JsonProperty("Services")
        private Services Services;

        @JsonProperty("ApplicantIdentifier")
        private String ApplicantIdentifier;
    }

    @lombok.Data
    public static class Identifiers {
        @JsonProperty("Identifier")
        private Identifier[] Identifier;
    }

    @lombok.Data
    public static class Identifier {
        @JsonProperty("IdNumber")
        private String IdNumber;

        @JsonProperty("IdType")
        private String IdType;
    }

    @lombok.Data
    public static class Telephones {
        @JsonProperty("Telephone")
        private Telephone Telephone;
    }

    @lombok.Data
    public static class Telephone {
        @JsonProperty("TelephoneNumber")
        private String TelephoneNumber;

        @JsonProperty("TelephoneType")
        private String TelephoneType;

        @JsonProperty("TelephoneCountryCode")
        private String TelephoneCountryCode;
    }

    @lombok.Data
    public static class Addresses {
        @JsonProperty("Address")
        private Address Address;
    }

    @lombok.Data
    public static class Address {
        @JsonProperty("AddressType")
        private String AddressType;

        @JsonProperty("AddressLine1")
        private String AddressLine1;

        @JsonProperty("AddressLine2")
        private String AddressLine2;

        @JsonProperty("AddressLine3")
        private String AddressLine3;

        @JsonProperty("AddressLine4")
        private String AddressLine4;

        @JsonProperty("AddressLine5")
        private String AddressLine5;

        @JsonProperty("City")
        private String City;

        @JsonProperty("PinCode")
        private String PinCode;

        @JsonProperty("ResidenceType")
        private String ResidenceType;

        @JsonProperty("StateCode")
        private String StateCode;
    }

    @lombok.Data
    public static class Accounts {
        @JsonProperty("Account")
        private Account Account;
    }
    @lombok.Data
    public static class Account {
        @JsonProperty("AccountNumber")
        private String AccountNumber;
    }

    @lombok.Data
    public static class Services {
        @JsonProperty("Service")
        private Service[] Service;
    }
    @lombok.Data
    public static class Service {
        @JsonProperty("Id")
        private String Id;

        @JsonProperty("Operations")
        private Operations Operations;

        @JsonProperty("Status")
        private String Status;

        @JsonProperty("Name")
        private String Name;
    }
    @lombok.Data
    public static class Services1 {
        @JsonProperty("Service")
        private Service1 Service;
    }

    @lombok.Data
    public static class Service1 {
        @JsonProperty("Id")
        private String Id;

        @JsonProperty("Consent")
        private String Consent;

        @JsonProperty("EnableSimulation")
        private String EnableSimulation;
        @JsonProperty("Skip")
        private String Skip;
        @JsonProperty("Name")
        private String Name;

    }


    @lombok.Data
    public static class Operations {
        @JsonProperty("Operation")
        private Operation[] Operation;
    }

    @lombok.Data
    public static class Operation {
        @JsonProperty("Name")
        private String Name;

        @JsonProperty("Params")
        private Params Params;

        @JsonProperty("Id")
        private String Id;

        @JsonProperty("Data")
        private Data Data;

        @JsonProperty("Status")
        private String Status;
    }

    @lombok.Data
    public static class Params {
        @JsonProperty("Param")
        private Param[] Param;
    }

    @lombok.Data
    public static class Param {
        @JsonProperty("Name")
        private String Name;

        @JsonProperty("Value")
        private String Value;
    }

    @lombok.Data
    public static class Data{
        @JsonProperty("Response")
        private Response Response;
    }

    @lombok.Data
    public static class Response {
        @JsonProperty("RawResponse")
        private RawResponse RawResponse;
    }

    @lombok.Data
    public static class RawResponse {
        @JsonProperty("DsMFICIR")
        private String DsMFICIR;

        @JsonProperty("Document")
        private Document Document;
    }

    @lombok.Data
    public static class Document {
        @JsonProperty("Id")
        private int Id;

        @JsonProperty("Name")
        private String Name;
    }

    @lombok.Data
    public static class ApplicationData {
        @JsonProperty("GSTStateCode")
        private String GSTStateCode;

        @JsonProperty("Services")
        private Services1 Services;

        @JsonProperty("GoSolutionSetId")
        private String GoSolutionSetId;

        @JsonProperty("GoSolutionSetVersion")
        private String GoSolutionSetVersion;
    }
}
