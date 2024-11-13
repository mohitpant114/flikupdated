package com.flik.request.cibilRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Telephones {
    @JsonProperty("Telephone")
    private Telephone telephone;
}
