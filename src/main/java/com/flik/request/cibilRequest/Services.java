package com.flik.request.cibilRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Services {

    @JsonProperty("Service")
    private Service service;
//    private List<Service> service;
    //private Service service;
}
