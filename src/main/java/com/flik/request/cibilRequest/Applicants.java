package com.flik.request.cibilRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Applicants {
//    private Applicant applicant;
@JsonProperty("Applicant")
private Applicant applicant;
}
