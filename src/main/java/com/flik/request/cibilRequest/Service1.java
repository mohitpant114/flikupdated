package com.flik.request.cibilRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Service1 {
    @JsonProperty("Id")
        private String id;
    @JsonProperty("Skip")
     private String skip;
    @JsonProperty("Constant")
    private String constant;
    @JsonProperty("EnableSimulation")
    private String enableSimulation;
}
