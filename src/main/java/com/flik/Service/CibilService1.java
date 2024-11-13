//package com.flik.Service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.dataformat.xml.XmlMapper;
//import com.google.gson.Gson;
//import com.flik.apiresource.CibilApiResource;
//import com.flik.constant.CibilUrl;
//import com.flik.entity.CreditScoreEntity;
//import com.flik.entity.CustomerEntity;
//import com.flik.entity.RejectCustomerEntity;
//import com.flik.model.CibilModel;
//import com.flik.repository.CreditScoreRepository;
//import com.flik.repository.CustomerRepository;
//import com.flik.repository.RejectCustomerRepository;
//import com.flik.request.RequestDto;
//import com.flik.respons.ApiResponseDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//@Service
//public class CibilService {
//
//    @Autowired
//    private CibilApiResource cibilApiResource;
//
//    @Autowired
//    private CreditScoreRepository creditScoreRepository;
//    @Autowired
//    private CustomerRepository customerRepository;
//    @Autowired
//    private RejectCustomerRepository rejectCustomerRepository;
//
//
//
//    public String createAndPostCibilRequest( CibilModel cibilModel,  Long customerId) throws JsonProcessingException {
//        Optional<CustomerEntity> customerOptional = customerRepository.findById( customerId);
//
//        if (!customerOptional.isPresent()) {
//            return "Customer not found";
//        }
//
//        CustomerEntity customer = customerOptional.get();
//        if (!"New".equalsIgnoreCase(customer.getStatus())) {
//            return "Customer status is not 'New'";
//        }
//
//
//        // Populate cibilModel with customer details
//        cibilModel.setApplicantFirstName(customer.getBorrowerName());
//        cibilModel.setGender(customer.getGender());
//       // cibilModel.set(customer.getEmailId());
//        cibilModel.setDateOfBirth(customer.getDob());
//        cibilModel.setAddressLine1(customer.getAddress());
//        cibilModel.setPinCode(customer.getPinCode());
//        cibilModel.setIdNumber(customer.getPanNumber());
//        cibilModel.setTelephoneNumber(customer.getMobileNumber());
//
//        String accessToken = cibilApiResource.getToken(CibilUrl.token);
//
//
//
//        RequestDto requestDto = cibilApiResource.createRequestDto(cibilModel);
//
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            String json = objectMapper.writeValueAsString(requestDto);
//            System.out.println(json);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//
//         String res = cibilApiResource.postToCibil(CibilUrl.cibil, accessToken, requestDto);
//
//
//
//
//
//        ApiResponseDto apiResponseDto = new Gson().fromJson(res, ApiResponseDto.class);
//
//
//
//        String dsMFICIR = apiResponseDto.getFields().getApplicants().getApplicant()
//                .getServices().getService()[0].getOperations().getOperation()[3]
//                .getData().getResponse().getRawResponse().getDsMFICIR();
//
//
//
//        System.out.println("DsMFICIR value: " + dsMFICIR);
//
//        XmlMapper xmlMapper = new XmlMapper();
//        JsonNode jsonNode = xmlMapper.readTree(dsMFICIR);
//        String jsonString = objectMapper.writeValueAsString(jsonNode);
//
//
//        System.out.println(jsonString);
//
//
//        CreditScoreEntity creditScoreEntity = new CreditScoreEntity();
//
//
//        creditScoreEntity.setCustomerId( customerId);
//        creditScoreEntity.setJson(jsonString);
//        String score = "NA";
//        try {
//            JsonNode node = new ObjectMapper().readTree(jsonString);
//            if (node.has("score")) {
//                JsonNode scoreNode = node.get("score");
//                if (scoreNode.has("informationData")) {
//                    JsonNode informationDataNode = scoreNode.get("informationData");
//                    if (informationDataNode.has("field")) {
//                        JsonNode fieldsArray = informationDataNode.get("field");
//                        for (JsonNode field : fieldsArray) {
//                            if (field.has("fieldKey") && "score".equals(field.get("fieldKey").asText())) {
//                                score = field.get("value").asText();
//                                creditScoreEntity.setScore(score);
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//
//
//        creditScoreEntity.setCustomerName(apiResponseDto.getFields().getApplicants().getApplicant().getApplicantFirstName());
//        creditScoreEntity.setMobileNumber(apiResponseDto.getFields().getApplicants().getApplicant().getTelephones().getTelephone().getTelephoneNumber());
//        creditScoreEntity.setAddress(apiResponseDto.getFields().getApplicants().getApplicant().getAddresses().getAddress().getAddressLine1());
//        creditScoreEntity.setPanNumber(apiResponseDto.getFields().getApplicants().getApplicant().getIdentifiers().getIdentifier()[0].getIdNumber());
//
//            if ("NA".equalsIgnoreCase(score) || Integer.parseInt(score) > 650) {
//                customer.setStatus("Credit Check");
//            } else {
//                customer.setStatus("Credit reject");
//             RejectCustomerEntity rejectEntity = new RejectCustomerEntity();
//
//
//                rejectEntity.setStatus("Credit Reject");
//                rejectEntity.setScore(score);
//                  rejectEntity.setReason("Score less than 650");
//
//
//                rejectEntity.setPartnerLoanId(customer.getPartnerLoanId());
//                rejectEntity.setFintechName(customer.getFintechName());
//                rejectEntity.setFintechId(customer.getFintechId());
//                rejectEntity.setBorrowerName(customer.getBorrowerName());
//                rejectEntity.setMobileNumber(customer.getMobileNumber());
//                rejectEntity.setEmail(customer.getEmail());
//                rejectEntity.setDob(customer.getDob().toString());
//                rejectEntity.setGender(customer.getGender());
//                rejectEntity.setMaritalStatus(customer.getMaritalStatus());
//                rejectEntity.setSpouseName(customer.getSpouseName());
//                rejectEntity.setNomineeName(customer.getNomineeName());
//                rejectEntity.setNomineeDob(customer.getNomineeDob());
//                rejectEntity.setNomineeGender(customer.getNomineeGender());
//                rejectEntity.setReferenceName(customer.getReferenceName());
//                rejectEntity.setReferenceMobileNumber(customer.getReferenceMobileNumber());
//                rejectEntity.setReferenceName2(customer.getReferenceName2());
//                rejectEntity.setReferenceMobileNumber2(customer.getReferenceMobileNumber2());
//                rejectEntity.setBranch(customer.getBranch());
//                rejectEntity.setPanNumber(customer.getPanNumber());
//                rejectEntity.setAadharNumber(customer.getAadharNumber());
//                rejectEntity.setResidentialType(customer.getResidentialType());
//                rejectEntity.setArea(customer.getArea());
//                rejectEntity.setPinCode(customer.getPinCode());
//                rejectEntity.setCity(customer.getCity());
//                rejectEntity.setState(customer.getState());
//                rejectEntity.setAddress(customer.getAddress());
//                rejectEntity.setLongitude(customer.getLongitude());
//                rejectEntity.setLatitude(customer.getLatitude());
//                rejectEntity.setCompanyName(customer.getCompanyName());
//                rejectEntity.setPosition(customer.getPosition());
//                rejectEntity.setCompanyAddress(customer.getCompanyAddress());
//                rejectEntity.setProfessionalEmail(customer.getProfessionalEmail());
//                rejectEntity.setCompanyType(customer.getCompanyType());
//                rejectEntity.setSalary(customer.getSalary());
//                rejectEntity.setAccountName(customer.getAccountName());
//                rejectEntity.setAccountNumber(customer.getAccountNumber());
//                rejectEntity.setIfscCode(customer.getIfscCode());
//                rejectEntity.setBankName(customer.getBankName());
//                rejectEntity.setBranchName(customer.getBranchName());
//                rejectEntity.setBureauDetails(customer.getBureauDetails());
//                rejectEntity.setBureauType(customer.getBureauType());
//                rejectEntity.setBureauScore(customer.getBureauScore());
//                rejectEntity.setNewToCredit(customer.getNewToCredit());
//                rejectEntity.setOtherDetails(customer.getOtherDetails());
//                rejectEntity.setOtherDetails2(customer.getOtherDetails2());
//                rejectEntity.setInsuranceCompany(customer.getInsuranceCompany());
//                rejectEntity.setCoBorrowerName(customer.getCoBorrowerName());
//                rejectEntity.setCoMobileNumber(customer.getCoMobileNumber());
//                rejectEntity.setCoEmail(customer.getCoEmail());
//                rejectEntity.setCoDob(customer.getCoDob());
//                rejectEntity.setCoGender(customer.getCoGender());
//                rejectEntity.setCoRelation(customer.getCoRelation());
//                rejectEntity.setCoEmploymentType(customer.getCoEmploymentType());
//                rejectEntity.setCoCompanyName(customer.getCoCompanyName());
//                rejectEntity.setCoPanNumber(customer.getCoPanNumber());
//                rejectEntity.setCoAadharNumber(customer.getCoAadharNumber());
//                rejectEntity.setCoBankName(customer.getCoBankName());
//                rejectEntity.setCoNameAsPerBank(customer.getCoNameAsPerBank());
//                rejectEntity.setCoAccountNumber(customer.getCoAccountNumber());
//                rejectEntity.setCoIfscCode(customer.getCoIfscCode());
//                rejectEntity.setCreatedDate(LocalDate.now()); // Set current date
//
//
//
//                rejectCustomerRepository.save(rejectEntity);
//
//        }
//        creditScoreRepository.save(creditScoreEntity);
//
//        customerRepository.save(customer);
//
//
//        } catch (Exception e) {
//            return "Error while processing credit score entity: " + e.getMessage();
//        }
//
//
//        return "Credit check successfully";
//    }
//
//
//}
//
//
//
////    public ApiResponseDto createAndPostCibilRequest(CibilModel cibilModel) throws JsonProcessingException {
////        String accessToken = cibilApiResource.getToken(CibilUrl.token);
////
////        RequestDto requestDto = cibilApiResource.createRequestDto(cibilModel);
////
////        ObjectMapper objectMapper = new ObjectMapper();
////        try {
////            String json = objectMapper.writeValueAsString(requestDto);
////            System.out.println(json);
////        } catch (JsonProcessingException e) {
////            e.printStackTrace();
////        }
////        String res = cibilApiResource.postToCibil(CibilUrl.cibil, accessToken, requestDto);
////
////        ApiResponseDto apiResponseDto = new Gson().fromJson(res, ApiResponseDto.class);
////        String dsMFICIR = apiResponseDto.getFields().getApplicants().getApplicant()
////                .getServices().getService()[0].getOperations().getOperation()[3]
////                .getData().getResponse().getRawResponse().getDsMFICIR();
////
////        System.out.println("DsMFICIR value: " + dsMFICIR);
////
////        XmlMapper xmlMapper = new XmlMapper();
////        JsonNode jsonNode = xmlMapper.readTree(dsMFICIR);
////
////
////        String jsonString = objectMapper.writeValueAsString(jsonNode);
////
////        System.out.println(jsonString);
////
////
////        return apiResponseDto;
////    }