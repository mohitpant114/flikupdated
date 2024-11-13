package com.flik.apiresource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flik.global.ApiResponseError;
import com.flik.model.AccountNumberModel;
import com.flik.request.AccountNumRequest;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountNumberApiResource {
    @Value("${ifsc.authorization}")
    private String authorization;


    private static final boolean FORCE_PENNY_DROP = false;
    private static final String FORCE_PENNY_DROP_AMOUNT = "1";

    private ObjectMapper objectMapper = new ObjectMapper();

    public String getAccountNumber(AccountNumRequest accountNumRequest, String url) {
        try {
            HttpResponse<String> response = Unirest.post(url)
                    .header("Authorization", authorization)
                    .header("Content-Type", "application/json")
                    .body(objectMapper.writeValueAsString(accountNumRequest))
                    .asString();
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiResponseError(404, "no response found");
        }
    }








    public AccountNumRequest createRequestDto(AccountNumberModel accountNumberModel) {

        AccountNumRequest accountNumRequest = new AccountNumRequest();
        accountNumRequest.setForce_penny_drop(FORCE_PENNY_DROP);
        accountNumRequest.setForce_penny_drop_amount(FORCE_PENNY_DROP_AMOUNT);

        String uniqueMerchantReferenceId = UUID.randomUUID().toString().substring(0, 7);
        accountNumRequest.setMerchant_reference_id(uniqueMerchantReferenceId);



        accountNumRequest.setBank_account_number(accountNumberModel.getBankAccountNumber());
        accountNumRequest.setBank_ifsc_code(accountNumberModel.getBankIfscCode());





        return accountNumRequest;
    }




    public String ifscCode(String ifsccode) {
        try {
            String url = "https://api.zwitch.io/v1/constants/ifsc-codes?start_after=555&start_before=2&results_per_page=1&ifsc_code=" + ifsccode;
            HttpResponse<String> response = Unirest.get(url)
                    .header("authorization", authorization)
                    .header("Content-Type", "application/json")
                    .asString();
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiResponseError(404, "no response found");
        }

    }

}
