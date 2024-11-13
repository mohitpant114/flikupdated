package com.flik.global;

import lombok.*;

@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder(toBuilder = true)
public class ApiResponseError extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private int code;
    private String message;
}
