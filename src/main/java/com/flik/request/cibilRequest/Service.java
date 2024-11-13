package com.flik.request.cibilRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class Service {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Operations")
    //private List<Operation> operations;
    private Operations operations;
//    private String id;
//    private Operations operations;
//    private String status;
//    private String name;
//    private String skip;
//    private String constant;
//    private String enableSimulation;
//public Service(String id, String skip, String consent, String enableSimulation) {
//    this.id = id;
//    this.skip = skip;
//    this.consent = consent;
//    this.enableSimulation = enableSimulation;
//}

//    public Service(String id, Operations operations) {
//        this.id = id;
//        this.operations = operations;
//    }
}
