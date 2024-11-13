package com.flik.apiresource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flik.global.ApiResponseError;
import com.flik.model.CibilModel;
import com.flik.request.RequestDto;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CibilApiResource {

    @Value("${cibil.api.username}")
    private String username;
    @Value("${cibil.api.password}")
    private String password;
    @Value("${cibil.api.grant_type}")
    private String grant_type;
    private ObjectMapper objectMapper = new ObjectMapper();


    public String getToken(String url) {
        try {
            String body = String.format("grant_type=password&username=%s&password=%s", username, password);

            HttpResponse<String> response = Unirest.post(url)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .body(body)
                    .asString();

            String jsonResponse = response.getBody();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            throw new ApiResponseError(404, "no response found");
        }
    }

    public String postToCibil(String url, String accessToken, RequestDto cibilRequest) {
        try {
            String jsonBody = objectMapper.writeValueAsString(cibilRequest);
            HttpResponse<String> response = Unirest.post(url)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .body(jsonBody)
                    .asString();

            return response.getBody();
        } catch (Exception e) {
            throw new ApiResponseError(404, "no response found");
        }
    }


    public RequestDto createRequestDto(CibilModel cibilModel) {
        RequestDto requestDto = new RequestDto();

        // Setting RequestInfo
        RequestDto.RequestInfo requestInfo = new RequestDto.RequestInfo();
        requestInfo.setSolutionSetName("GO_SALORA_CAPITAL_AGSS");
        requestInfo.setExecuteLatestVersion("true");
        requestDto.setRequestInfo(requestInfo);

        // Setting Fields
        RequestDto.Fields fields = new RequestDto.Fields();
        RequestDto.Applicants applicants = new RequestDto.Applicants();
        RequestDto.Applicant applicant = new RequestDto.Applicant();

        applicant.setApplicantFirstName(cibilModel.getApplicantFirstName());
        applicant.setApplicantMiddleName(cibilModel.getApplicantMiddleName());
        applicant.setApplicantLastName(cibilModel.getApplicantLastName());
        applicant.setDateOfBirth(cibilModel.getDateOfBirth());
        applicant.setGender(cibilModel.getGender());
        applicant.setEmailAddress("");

        // Setting Identifiers
        RequestDto.Identifiers identifiers = new RequestDto.Identifiers();
        RequestDto.Identifier identifier = new RequestDto.Identifier();
        identifier.setIdNumber(cibilModel.getIdNumber());
        identifier.setIdType("01");
        identifiers.setIdentifier(Arrays.asList(identifier));
        applicant.setIdentifiers(identifiers);

        // Setting Telephones
        RequestDto.Telephones telephones = new RequestDto.Telephones();
        RequestDto.Telephone telephone = new RequestDto.Telephone();
        telephone.setTelephoneNumber(cibilModel.getTelephoneNumber());
        telephone.setTelephoneType("01");
        telephone.setTelephoneCountryCode("91");
        telephones.setTelephone(telephone);
        applicant.setTelephones(telephones);

        // Setting Addresses
        RequestDto.Addresses addresses = new RequestDto.Addresses();
        RequestDto.Address address = new RequestDto.Address();
        address.setAddressType("01");
        address.setAddressLine1(cibilModel.getAddressLine1());
        address.setAddressLine2(cibilModel.getAddressLine2());
        address.setAddressLine3("");
        address.setAddressLine4("");
        address.setAddressLine5("");
        address.setCity("");
        address.setPinCode(cibilModel.getPinCode());
        address.setResidenceType("01");
        address.setStateCode(cibilModel.getStateCode());
        addresses.setAddress(address);
        applicant.setAddresses(addresses);

        // Setting other fields
        applicant.setKeyPersonRelation("");
        applicant.setKeyPersonName("");
        applicant.setMemberRelationName1("test3");
        applicant.setMemberRelationType1("test4");
        applicant.setMemberRelationName2("test5");
        applicant.setMemberRelationType2("test6");
        applicant.setMemberRelationName3("test7");
        applicant.setMemberRelationType3("test8");
        applicant.setMemberRelationName4("test9");
        applicant.setMemberRelationType4("test10");
        applicant.setNomineeName("test11");
        applicant.setNomineeRelation("test12");
        applicant.setMemberOtherId1("test13");
        applicant.setMemberOtherId1Type("test14");
        applicant.setMemberOtherId2("test15");
        applicant.setMemberOtherId2Type("test16");
        applicant.setMemberOtherId3("test17");
        applicant.setMemberOtherId3Type("test18");
        applicant.setMemberOtherId4("test19");
        applicant.setMemberOtherId4Type("test20");

        // Setting Accounts
        RequestDto.Accounts accounts = new RequestDto.Accounts();
        RequestDto.Account account = new RequestDto.Account();
        account.setAccountNumber("test21");
        accounts.setAccount(account);
        applicant.setAccounts(accounts);

        // Setting Services
        RequestDto.Services services = new RequestDto.Services();
        RequestDto.Service service = new RequestDto.Service();
        service.setId("CORE");

        RequestDto.Operations operations = new RequestDto.Operations();
        RequestDto.Operation operation1 = new RequestDto.Operation();
        operation1.setName("ConsumerCIR");
        RequestDto.Params params1 = new RequestDto.Params();
        RequestDto.Param param1 = new RequestDto.Param();
        param1.setName("CibilBureauFlag");
        param1.setValue("true");
        RequestDto.Param param2 = new RequestDto.Param();
        param2.setName("Amount");
        param2.setValue("400000");
        RequestDto.Param param3 = new RequestDto.Param();
        param3.setName("Purpose");
        param3.setValue("10");
        RequestDto.Param param4 = new RequestDto.Param();
        param4.setName("ScoreType");
        param4.setValue("08");
        RequestDto.Param param5 = new RequestDto.Param();
        param5.setName("MemberCode");
        param5.setValue("");
        RequestDto.Param param6 = new RequestDto.Param();
        param6.setName("Password");
        param6.setValue("");
        RequestDto.Param param7 = new RequestDto.Param();
        param7.setName("FormattedReport");
        param7.setValue("true");
        RequestDto.Param param8 = new RequestDto.Param();
        param8.setName("GSTStateCode");
        param8.setValue("");
        params1.setParam(Arrays.asList(param1, param2, param3, param4, param5, param6, param7, param8));
        operation1.setParams(params1);

        RequestDto.Operation operation2 = new RequestDto.Operation();
        operation2.setName("IDV");
        RequestDto.Params params2 = new RequestDto.Params();
        RequestDto.Param param9 = new RequestDto.Param();
        param9.setName("IDVerificationFlag");
        param9.setValue("false");
        RequestDto.Param param10 = new RequestDto.Param();
        param10.setName("ConsumerConsentForUIDAIAuthentication");
        param10.setValue("N");
        RequestDto.Param param11 = new RequestDto.Param();
        param11.setName("GSTStateCode");
        param11.setValue("33");
        params2.setParam(Arrays.asList(param9, param10, param11));
        operation2.setParams(params2);

        RequestDto.Operation operation3 = new RequestDto.Operation();
        operation3.setName("NTC");
        RequestDto.Params params3 = new RequestDto.Params();
        RequestDto.Param param12 = new RequestDto.Param();
        param12.setName("DSTuNtcFlag");
        param12.setValue("false");
        RequestDto.Param param13 = new RequestDto.Param();
        param13.setName("NTCProductType");
        param13.setValue("CC");

        params3.setParam(Arrays.asList(param12, param13));
        operation3.setParams(params3);

        RequestDto.Operation operation4 = new RequestDto.Operation();
        operation4.setName("MFI");
        RequestDto.Params params4 = new RequestDto.Params();
        RequestDto.Param param14 = new RequestDto.Param();
        param14.setName("MFIBureauFlag");
        param14.setValue("false");
        RequestDto.Param param15 = new RequestDto.Param();
        param15.setName("MFIEnquiryAmount");
        param15.setValue("35672");
        RequestDto.Param param16 = new RequestDto.Param();
        param16.setName("MFILoanPurpose");
        param16.setValue("42");
        RequestDto.Param param17 = new RequestDto.Param();
        param17.setName("MFICenterReferenceNo");
        param17.setValue("MRN39359");
        RequestDto.Param param18 = new RequestDto.Param();
        param18.setName("MFIBranchReferenceNo");
        param18.setValue("BRANCH3217");
        RequestDto.Param param19 = new RequestDto.Param();
        param19.setName("GSTStateCode");
        param19.setValue("33");


        params4.setParam(Arrays.asList(param14, param15, param16, param17, param18, param19));
        operation4.setParams(params4);


        operations.setOperation(Arrays.asList(operation1, operation2, operation3, operation4));
        service.setOperations(operations);
        services.setService(service);
        applicant.setServices(services);

        RequestDto.ApplicationData applicationData = new RequestDto.ApplicationData();
        applicationData.setGSTStateCode("27");
        RequestDto.Service1 x = new RequestDto.Service1();
        x.setId("CORE");
        x.setConsent("true");
        x.setEnableSimulation("False");
        x.setSkip("N");
        applicationData.setServices(new RequestDto.Services1(x));

        applicants.setApplicant(applicant);
        fields.setApplicants(applicants);
        fields.setApplicationData(applicationData);
        requestDto.setFields(fields);

        return requestDto;
    }







    public byte[] downloadPdf(String documentId, String token) {
        HttpResponse<byte[]> response = Unirest.get("https://dc.cibil.com/DE/TU.DE.Pont/Documents/" + documentId)
                .header("Authorization", "Bearer " + token)
                .header("Cookie", "__cf_bm=qUjX0ycF9Wd_g4rkOj644ZwzZ6iyo8Fz.KOQ0d8D_gg-1722418590-1.0.1.1-z2aWQopJi.2KekxBc_zuyHnhcyDsJcnEa4lhFzsixP.8x3AmpQc3pZm83kqpWcIRgo9bywZursQZq3JkmIG75Q; tu_cookie=!0mCeTiVz2Lm2qFVs1ITlpN4h8nkvq4eEythEylrbgXNdhQqyZHT6TTIgooO3ltACgNhD4PWEQM5jMA==")
                .asBytes();

        if (response.getStatus() == 200) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to download PDF: " + response.getStatusText());
        }
    }
























    //    public HttpResponse<InputStream> downloadPdf(String documentId, String accessToken, String url) {
//    //    String url = "https://dc.cibil.com/DE/TU.DE.Pont/Documents/" + documentId;
//
//        return Unirest.get(url + documentId)
//                .header("Authorization", "Bearer " + accessToken)
//                .header("Accept", "application/pdf")
//                .asObject(InputStream.class);
//    }
//
//    public HttpResponse<InputStream> downloadPdf(String documentId, String accessToken , String baseUrl) {
//        if (baseUrl == null || baseUrl.trim().isEmpty()) {
//            throw new IllegalArgumentException("Base URL cannot be null or empty");
//        }
//         String encodedDocumentId = URLEncoder.encode(documentId, StandardCharsets.UTF_8);
//        String url = baseUrl + encodedDocumentId;
//
//        return Unirest.get(url)
//                .header("Authorization", "Bearer " + accessToken)
//                .header("Accept", "application/pdf")
//                .asObject(InputStream.class);
//    }
//    public InputStream downloadPdf(String documentId, String accessToken, String baseUrl) throws Exception {
//        if (baseUrl == null || baseUrl.trim().isEmpty()) {
//            throw new IllegalArgumentException("Base URL cannot be null or empty");
//        }
//
//
//        String encodedDocumentId = URLEncoder.encode(documentId, StandardCharsets.UTF_8);
//
//
//        String url = baseUrl + encodedDocumentId;
//
//
//        System.out.println("Request URL: " + url);
//
//
//        HttpResponse<InputStream> response = Unirest.get(url)
//                .header("Authorization", "Bearer " + accessToken)
//                .header("Accept", "application/pdf")
//                .asObject(InputStream.class);
//
//        if (response.getStatus() != 200) {
//            throw new RuntimeException("Failed to download PDF. HTTP error code: " + response.getStatus());
//        }
//
//
//        // Return the InputStream
//        return response.getBody();
//    }
}
















