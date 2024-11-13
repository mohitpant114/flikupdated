package com.flik.request.cibilRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {


//    private String addressType;
//    private String addressLine1;
//    private String addressLine2;
//    private String pinCode;
//    private String residenceType;
//    private String stateCode;
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
