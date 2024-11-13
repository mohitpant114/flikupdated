package com.flik.request.cibilRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Identifier {

    @JsonProperty("IdNumber")
    private String idNumber;

    @JsonProperty("IdType")
    private String idType;

}
