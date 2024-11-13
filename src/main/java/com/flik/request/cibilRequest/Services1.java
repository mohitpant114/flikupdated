package com.flik.request.cibilRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Services1 {
    @JsonProperty("Service")
    private Service1 service;
}
