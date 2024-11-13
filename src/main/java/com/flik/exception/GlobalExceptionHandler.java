package com.flik.exception;

import com.flik.global.APIResponse;
import com.flik.global.ApiResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleAllUncaughtException(RuntimeException ex)
    {
        return new ResponseEntity<>(exceptionResponse(500, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static APIResponse exceptionResponse(int code, String error){
        return APIResponse.builder().success(false).error(ApiResponseError.builder().code(code).message(error).build()).build();
    }
}
