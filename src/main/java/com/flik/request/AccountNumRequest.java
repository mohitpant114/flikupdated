package com.flik.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AccountNumRequest {
    private boolean force_penny_drop;
    private String bank_account_number;
    private String bank_ifsc_code;
    private String force_penny_drop_amount;
    private String merchant_reference_id;
}
