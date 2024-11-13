package com.flik.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class UserLoginRequest {
    private String emailOrMobileNumber;
    private String password;

}
