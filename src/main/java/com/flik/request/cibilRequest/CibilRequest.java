package com.flik.request.cibilRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CibilRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("Fields")
    private Fields fields;
//    private RequestInfo requestInfo;
//    private Fields fields;
   // private ApplicationData applicationData;
}
