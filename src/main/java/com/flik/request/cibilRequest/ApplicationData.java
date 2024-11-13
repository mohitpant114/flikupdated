package com.flik.request.cibilRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationData {

    @JsonProperty("GSTStateCode")
    private String gstStateCode;

    @JsonProperty("Services")
    private Services1 services;
}
