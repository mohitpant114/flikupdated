package com.flik.request.cibilRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Applicant {

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
    private  Addresses addresses;

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





//    private String applicantFirstName;
//    private String applicantMiddleName;
//    private String applicantLastName;
//    private String dateOfBirth;
//    private String gender;
//    private String emailAddress;
//    private Identifiers identifiers;
//    private Telephones telephones;
//    private Addresses addresses;
//    private String keyPersonRelation;
//    private String keyPersonName;
//    private List<MemberRelation> memberRelations;
//    private String nomineeName;
//    private String nomineeRelation;
//    private List<MemberOtherId> memberOtherIds;
//    private Accounts accounts;
//    private Services services;
//
//
//    private String memberRelationName1;
//    private String memberRelationType1;
//    private String memberRelationName2;
//    private String memberRelationType2;
//    private String memberRelationName3;
//    private String memberRelationType3;
//    private String memberRelationName4;
//    private String memberRelationType4;
//    private String memberOtherId1;
//    private String memberOtherId1Type;
//    private String memberOtherId2;
//    private String memberOtherId2Type;
//    private String memberOtherId3;
//    private String memberOtherId3Type;
//    private String memberOtherId4;
//    private String memberOtherId4Type;

//    private String applicantFirstName;
//    private String applicantMiddleName;
//    private String applicantLastName;
//    private String dateOfBirth;
//    private String gender;
//    private Identifiers identifiers;
//    private Telephones telephones;
//    private Addresses addresses;
//    private String keyPersonRelation;
//    private String keyPersonName;
//    private String memberRelationName1;
//    private String memberRelationType1;
//    private String memberRelationName2;
//    private String memberRelationType2;
//    private String memberRelationName3;
//    private String memberRelationType3;
//    private String memberRelationName4;
//    private String memberRelationType4;
//    private String nomineeName;
//    private String nomineeRelation;
//    private String memberOtherId1;
//    private String memberOtherId1Type;
//    private String memberOtherId2;
//    private String memberOtherId2Type;
//    private String memberOtherId3;
//    private String memberOtherId3Type;
//    private String memberOtherId4;
//    private String memberOtherId4Type;
//    private Accounts accounts;
//    private Services services;

}
