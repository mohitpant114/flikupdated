package com.flik.request.cibilRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Param {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("Value")
    private String value;
}
