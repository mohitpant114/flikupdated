package com.flik.request.cibilRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestInfo {
    @JsonProperty("SolutionSetName")
    private String solutionSetName;

    @JsonProperty("ExecuteLatestVersion")
    private String executeLatestVersion;

}
