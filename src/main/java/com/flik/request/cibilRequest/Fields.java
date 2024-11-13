package com.flik.request.cibilRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Fields {
    @JsonProperty("Applicants")
    private Applicants applicants;

    @JsonProperty("ApplicationData")
    private ApplicationData applicationData;
}
