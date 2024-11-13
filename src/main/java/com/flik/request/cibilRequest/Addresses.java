package com.flik.request.cibilRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Addresses {
    @JsonProperty("Address")
    private Address address;
}
