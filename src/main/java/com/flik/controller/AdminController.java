package com.flik.controller;

import com.flik.Service.AdminService;
import com.flik.request.LoginRequest;
import com.flik.respons.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")

public class AdminController {


    @Autowired
    private AdminService adminService;


    @PostMapping("/login")

    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = adminService.authenticate(loginRequest);
        if (isAuthenticated) {
            return ResponseEntity.ok(new LoginResponse(true, "Login successful, redirect to dashboard."));

        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"Access to the site\", charset=\"UTF-8\"");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .headers(headers)
                    .body(new LoginResponse(false, "Invalid credentials, please try again."));

        }


    }


    


}

