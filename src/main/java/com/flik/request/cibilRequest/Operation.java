package com.flik.request.cibilRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Operation {
//    private String name;
//  //  private List<Param> params;
//    private Params params;
//
//    public Operation(String name, List<Param> params) {
//        this.name = name;
//        this.params = (Params) params;
//    }
//    private String name;
//    private List<Param> params;


    @JsonProperty("Name")
    private String name;
    @JsonProperty("Params")
    private  Params params;



//    @JsonProperty("Params")
//    private List<Param> params;


//    public Operation(String name, List<Param> params) {
//        this.name = name;
//        this.params = params;
//    }
}
