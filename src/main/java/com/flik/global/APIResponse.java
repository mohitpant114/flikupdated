package com.flik.global;

import lombok.*;

@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder(toBuilder = true)
public class APIResponse {
    private boolean success;
    private Object data;
    private ApiResponseError error;
    private String statusCode;
}
