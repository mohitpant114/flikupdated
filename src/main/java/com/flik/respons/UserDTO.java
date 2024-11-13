package com.flik.respons;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserDTO {

    private String fintechId;
    private String fintechName;
    private String spocMobileNumber;
    private String directorName;
    private String directorPan;
    private String gstNumber;

}