package com.flik.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AccountNumberModel {

    private String bankAccountNumber;
    private String bankIfscCode;

}
