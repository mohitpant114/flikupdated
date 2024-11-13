package com.flik.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
@Component
public class APIGateWay {

    public ResponseEntity<APIResponse> handleRequest(Object obj){

        APIResponse apiResponse = APIResponse.builder().data(obj).success(true).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
