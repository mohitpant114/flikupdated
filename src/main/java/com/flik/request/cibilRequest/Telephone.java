package com.flik.request.cibilRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Telephone {
    @JsonProperty("TelephoneNumber")
    private String telephoneNumber;

    @JsonProperty("TelephoneType")
    private String telephoneType;

    @JsonProperty("TelephoneCountryCode")
    private String telephoneCountryCode;
}
