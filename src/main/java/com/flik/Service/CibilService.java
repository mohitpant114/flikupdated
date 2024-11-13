package com.flik.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import com.flik.apiresource.CibilApiResource;
import com.flik.constant.CibilUrl;
import com.flik.entity.CreditScoreEntity;
import com.flik.entity.CustomerEntity;
import com.flik.entity.RejectCustomerEntity;
import com.flik.model.CibilModel;
import com.flik.repository.CreditScoreRepository;
import com.flik.repository.CustomerRepository;
import com.flik.repository.RejectCustomerRepository;
import com.flik.request.RequestDto;
import com.flik.respons.ApiResponseDto;
import com.flik.respons.RejectCustomerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
public class CibilService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CibilApiResource cibilApiResource;

    @Autowired
    private CreditScoreRepository creditScoreRepository;

    @Autowired
    private RejectCustomerRepository rejectCustomerRepository;
    private static final Logger logger = LoggerFactory.getLogger(CibilService.class);

    public String createAndPostCibilRequest(CibilModel cibilModel, Long customerId) throws IOException {
        Optional<CustomerEntity> customerOptional = customerRepository.findById(customerId);

        if (!customerOptional.isPresent()) {
            return "Customer not found";
        }

        CustomerEntity customer = customerOptional.get();
        if (!"New".equalsIgnoreCase(customer.getStatus())) {
            return "Customer status is not 'New'";
        }


        cibilModel.setApplicantFirstName(customer.getBorrowerName());
        cibilModel.setGender(customer.getGender());
        cibilModel.setDateOfBirth(customer.getDob());
        cibilModel.setAddressLine1(customer.getAddress());
        cibilModel.setPinCode(customer.getPinCode());
        cibilModel.setIdNumber(customer.getPanNumber());
        cibilModel.setTelephoneNumber(customer.getMobileNumber());


        String accessToken = cibilApiResource.getToken(CibilUrl.token);


        RequestDto requestDto = cibilApiResource.createRequestDto(cibilModel);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String json = objectMapper.writeValueAsString(requestDto);
            System.out.println("Request JSON: " + json);
        } catch (JsonProcessingException e) {
            return "Failed to process request JSON: " + e.getMessage();
        }


        String res = cibilApiResource.postToCibil(CibilUrl.cibil, accessToken, requestDto);
        System.out.println("API Response: " + res);

        ApiResponseDto apiResponseDto = new Gson().fromJson(res, ApiResponseDto.class);

        String documentId = String.valueOf(apiResponseDto.getFields().getApplicants().getApplicant()
                .getServices().getService()[0].getOperations().getOperation()[3].getData().getResponse().getRawResponse()
                .getDocument().getId());
//        String documentName= apiResponseDto.getFields().getApplicants().getApplicant()
//                .getServices().getService()[0].getOperations().getOperation()[3].getData().getResponse().getRawResponse()         .getDocument().getName();

        String dsMFICIR = apiResponseDto.getFields().getApplicants().getApplicant()
                .getServices().getService()[0].getOperations().getOperation()[3]
                .getData().getResponse().getRawResponse().getDsMFICIR();
        System.out.println("dsMFICIR: " + dsMFICIR);


        XmlMapper xmlMapper = new XmlMapper();
        String jsonString;
        try {
            JsonNode jsonNode = xmlMapper.readTree(dsMFICIR);
            jsonString = objectMapper.writeValueAsString(jsonNode);
            System.out.println("Converted JSON: " + jsonString);
        } catch (Exception e) {
            return "Failed to convert dsMFICIR to JSON: " + e.getMessage();
        }

        CreditScoreEntity creditScoreEntity = creditScoreRepository.findByCustomerId(customerId)
                .orElse(new CreditScoreEntity());
        creditScoreEntity.setCustomerId(customerId);
        creditScoreEntity.setJson(jsonString);
        creditScoreEntity.setDocumentId(documentId);


        String score = null;
        String mfiScore = null;
        String plScore = null;
        try {
            JsonNode node = new ObjectMapper().readTree(jsonString);
            if (node.has("score")) {
                JsonNode scoreNode = node.get("score");
                if (scoreNode.has("informationData")) {
                    JsonNode informationDataNode = scoreNode.get("informationData");
                    if (informationDataNode.has("field")) {
                        JsonNode fieldsArray = informationDataNode.get("field");
                        for (JsonNode field : fieldsArray) {
                            if (field.has("fieldKey") && "score".equals(field.get("fieldKey").asText())) {
                                score = field.get("value").asText();
                                creditScoreEntity.setScore(score);
                                break;
                            }
                        }
                    }
                }
            }
            if (node.has("plScore")) {
                JsonNode scoreNode = node.get("plScore");
                if (scoreNode.has("informationData")) {
                    JsonNode informationDataNode = scoreNode.get("informationData");
                    if (informationDataNode.has("field")) {
                        JsonNode fieldsArray = informationDataNode.get("field");
                        for (JsonNode field : fieldsArray) {
                            if (field.has("fieldKey") && "score".equals(field.get("fieldKey").asText())) {
                                mfiScore = field.get("value").asText();
                                creditScoreEntity.setMfiScore(mfiScore);
                                break;
                            }
                        }
                    }
                }
            }
            if (node.has("mfiScore")) {
                JsonNode scoreNode = node.get("mfiScore");
                if (scoreNode.has("informationData")) {
                    JsonNode informationDataNode = scoreNode.get("informationData");
                    if (informationDataNode.has("field")) {
                        JsonNode fieldsArray = informationDataNode.get("field");
                        for (JsonNode field : fieldsArray) {
                            if (field.has("fieldKey") && "score".equals(field.get("fieldKey").asText())) {
                                plScore = field.get("value").asText();
                                creditScoreEntity.setPlScore(plScore);
                                break;
                            }
                        }
                    }
                }
            }



            creditScoreEntity.setCustomerName(apiResponseDto.getFields().getApplicants().getApplicant().getApplicantFirstName());
            creditScoreEntity.setMobileNumber(apiResponseDto.getFields().getApplicants().getApplicant().getTelephones().getTelephone().getTelephoneNumber());
            creditScoreEntity.setAddress(apiResponseDto.getFields().getApplicants().getApplicant().getAddresses().getAddress().getAddressLine1());
            creditScoreEntity.setPanNumber(apiResponseDto.getFields().getApplicants().getApplicant().getIdentifiers().getIdentifier()[0].getIdNumber());
            creditScoreEntity.setCreatedDate(LocalDate.now());

            creditScoreRepository.save(creditScoreEntity);

            customer.setBureauScore(score);
            customer.setBureauType("Cibil");
            customer.setBureauDetails("MFI");



            if (score == null || "NA".equals(score) || Integer.parseInt(score) >= 650) {
                customer.setStatus("Credit check");
                creditScoreEntity.setStatus("Credit check");

                customerRepository.save(customer);
                return "Credit check successfully";

            } else if (Integer.parseInt(score) <= 649) {
                customer.setStatus("Credit reject");
                creditScoreEntity.setStatus("Credit reject");

                customerRepository.save(customer);

                creditScoreRepository.save(creditScoreEntity);


                RejectCustomerEntity rejectCustomer = new RejectCustomerEntity();

                rejectCustomer.setStatus("Credit reject");
               // rejectCustomer.setScore(score);
                rejectCustomer.setReason("Score less than 650");
                //rejectCustomer.setJson(jsonString);
                rejectCustomer.setJson(jsonString.length() > 255 ? jsonString.substring(0, 255) : jsonString);


                rejectCustomer.setCustomerId(customerId);
                rejectCustomer.setBureauDetails("MFI");
                rejectCustomer.setBureauType("Cibil");
                rejectCustomer.setBureauScore(score);

                rejectCustomer.setPartnerLoanId(customer.getPartnerLoanId());
                rejectCustomer.setFintechName(customer.getFintechName());
//                rejectCustomer.setFintechId(customer.getFintechId());
                rejectCustomer.setBorrowerName(customer.getBorrowerName());
                rejectCustomer.setMobileNumber(customer.getMobileNumber());
                rejectCustomer.setEmail(customer.getEmail());
                rejectCustomer.setDob(customer.getDob());
                rejectCustomer.setGender(customer.getGender());
//                rejectCustomer.setMaritalStatus(customer.getMaritalStatus());
//                rejectCustomer.setSpouseName(customer.getSpouseName());
//                rejectCustomer.setNomineeName(customer.getNomineeName());
//                rejectCustomer.setNomineeDob(customer.getNomineeDob());
//                rejectCustomer.setNomineeGender(customer.getNomineeGender());
//                rejectCustomer.setReferenceName(customer.getReferenceName());
//                rejectCustomer.setReferenceMobileNumber(customer.getReferenceMobileNumber());
//                rejectCustomer.setReferenceName2(customer.getReferenceName2());
//                rejectCustomer.setReferenceMobileNumber2(customer.getReferenceMobileNumber2());
//                rejectCustomer.setBranch(customer.getBranch());
                rejectCustomer.setPanNumber(customer.getPanNumber());
//                rejectCustomer.setAadharNumber(customer.getAadharNumber());
                rejectCustomer.setDobJoining(customer.getDobJoining());
//                rejectCustomer.setResidentialType(customer.getResidentialType());
//                rejectCustomer.setArea(customer.getArea());
                rejectCustomer.setPinCode(customer.getPinCode());
                rejectCustomer.setCity(customer.getCity());
                rejectCustomer.setState(customer.getState());
                rejectCustomer.setAddress(customer.getAddress());
//                rejectCustomer.setLongitude(customer.getLongitude());
//                rejectCustomer.setLatitude(customer.getLatitude());
                rejectCustomer.setCompanyName(customer.getCompanyName());
                rejectCustomer.setDobJoining(customer.getDobJoining());
                rejectCustomer.setFatherName(customer.getFatherName());
                rejectCustomer.setCompanyAddress(customer.getCompanyAddress());
//                rejectCustomer.setProfessionalEmail(customer.getProfessionalEmail());
//                rejectCustomer.setCompanyType(customer.getCompanyType());
                rejectCustomer.setSalary(customer.getSalary());
                rejectCustomer.setAccountName(customer.getAccountName());
                rejectCustomer.setAccountNumber(customer.getAccountNumber());
                rejectCustomer.setIfscCode(customer.getIfscCode());
                rejectCustomer.setBankName(customer.getBankName());
//                rejectCustomer.setBranchName(customer.getBranchName());
                rejectCustomer.setNewToCredit(customer.getNewToCredit());
                rejectCustomer.setOtherDetails(customer.getOtherDetails());
                rejectCustomer.setOtherDetails2(customer.getOtherDetails2());
//                rejectCustomer.setInsuranceCompany(customer.getInsuranceCompany());
//                rejectCustomer.setCoBorrowerName(customer.getCoBorrowerName());
//                rejectCustomer.setCoMobileNumber(customer.getCoMobileNumber());
//                rejectCustomer.setCoEmail(customer.getCoEmail());
//                rejectCustomer.setCoDob(customer.getCoDob());
//                rejectCustomer.setCoGender(customer.getCoGender());
//                rejectCustomer.setCoRelation(customer.getCoRelation());
//                rejectCustomer.setCoEmploymentType(customer.getCoEmploymentType());
//                rejectCustomer.setCoCompanyName(customer.getCoCompanyName());
//                rejectCustomer.setCoPanNumber(customer.getCoPanNumber());
//                rejectCustomer.setCoAadharNumber(customer.getCoAadharNumber());
//                rejectCustomer.setCoBankName(customer.getCoBankName());
//                rejectCustomer.setCoNameAsPerBank(customer.getCoNameAsPerBank());
//                rejectCustomer.setCoAccountNumber(customer.getCoAccountNumber());
//                rejectCustomer.setCoIfscCode(customer.getCoIfscCode());
                rejectCustomer.setCreatedDate(LocalDate.now());


                rejectCustomerRepository.save(rejectCustomer);
                return "Credit rejected due to low score";
            }
        } catch (Exception e) {
            return "Error while processing credit score entity: " + e.getMessage();
        }

        return "Credit process completed";
    }









    public byte[] getPdf( Long customerId) {
        CreditScoreEntity creditScore = creditScoreRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("CreditScore not found for customer ID: " + customerId));
        String documentId = creditScore.getDocumentId();

        if (documentId == null || documentId.isEmpty()) {
            throw new RuntimeException("Document ID not found for customer ID: " + customerId);
        }
        String token = cibilApiResource.getToken(CibilUrl.token);

        return cibilApiResource.downloadPdf(documentId, token);
    }





    public List<RejectCustomerEntity> getAllRejectList() {

        return rejectCustomerRepository.findAll();
    }



    public RejectCustomerResponse getRejectListById(Long rejectId) {
        RejectCustomerEntity rejectCustomer = rejectCustomerRepository.findById(rejectId)
                .orElseThrow(() -> new RuntimeException("Reject Customer not found with ID: " + rejectId));

        RejectCustomerResponse rejectCustomerResponse= new RejectCustomerResponse();

        rejectCustomerResponse.setId(rejectCustomer.getId());
        rejectCustomerResponse.setId(rejectCustomer.getId());
        rejectCustomerResponse.setCustomerId(rejectCustomer.getCustomerId());
        rejectCustomerResponse.setStatus(rejectCustomer.getStatus());
       // rejectCustomerResponse.setScore(rejectCustomer.getScore());
        rejectCustomerResponse.setReason(rejectCustomer.getReason());
        rejectCustomerResponse.setPartnerLoanId(rejectCustomer.getPartnerLoanId());
        rejectCustomerResponse.setFintechName(rejectCustomer.getFintechName());
        rejectCustomerResponse.setFintechId(rejectCustomer.getFintechId());
        rejectCustomerResponse.setBorrowerName(rejectCustomer.getBorrowerName());
        rejectCustomerResponse.setMobileNumber(rejectCustomer.getMobileNumber());
        rejectCustomerResponse.setEmail(rejectCustomer.getEmail());
        rejectCustomerResponse.setDob(rejectCustomer.getDob());
        rejectCustomerResponse.setGender(rejectCustomer.getGender());
        rejectCustomerResponse.setDobJoining(rejectCustomer.getDobJoining());
        rejectCustomerResponse.setFatherName(rejectCustomer.getFatherName());
//        rejectCustomerResponse.setMaritalStatus(rejectCustomer.getMaritalStatus());
//        rejectCustomerResponse.setSpouseName(rejectCustomer.getSpouseName());
//        rejectCustomerResponse.setNomineeName(rejectCustomer.getNomineeName());
//        rejectCustomerResponse.setNomineeDob(rejectCustomer.getNomineeDob());
//        rejectCustomerResponse.setNomineeGender(rejectCustomer.getNomineeGender());
//        rejectCustomerResponse.setReferenceName(rejectCustomer.getReferenceName());
//        rejectCustomerResponse.setReferenceMobileNumber(rejectCustomer.getReferenceMobileNumber());
//        rejectCustomerResponse.setReferenceName2(rejectCustomer.getReferenceName2());
//        rejectCustomerResponse.setReferenceMobileNumber2(rejectCustomer.getReferenceMobileNumber2());
//        rejectCustomerResponse.setBranch(rejectCustomer.getBranch());
        rejectCustomerResponse.setPanNumber(rejectCustomer.getPanNumber());
        rejectCustomerResponse.setAadharNumber(rejectCustomer.getAadharNumber());
//        rejectCustomerResponse.setResidentialType(rejectCustomer.getResidentialType());
//        rejectCustomerResponse.setArea(rejectCustomer.getArea());
        rejectCustomerResponse.setPinCode(rejectCustomer.getPinCode());
        rejectCustomerResponse.setCity(rejectCustomer.getCity());
        rejectCustomerResponse.setState(rejectCustomer.getState());
        rejectCustomerResponse.setAddress(rejectCustomer.getAddress());
        rejectCustomerResponse.setLongitude(rejectCustomer.getLongitude());
        rejectCustomerResponse.setLatitude(rejectCustomer.getLatitude());
        rejectCustomerResponse.setCompanyName(rejectCustomer.getCompanyName());
//        rejectCustomerResponse.setPosition(rejectCustomer.getPosition());
        rejectCustomerResponse.setCompanyAddress(rejectCustomer.getCompanyAddress());
        rejectCustomerResponse.setDobJoining(rejectCustomer.getDobJoining());
        rejectCustomerResponse.setFatherName(rejectCustomer.getFatherName());
        rejectCustomerResponse.setSalary(rejectCustomer.getSalary());
        rejectCustomerResponse.setAccountName(rejectCustomer.getAccountName());
        rejectCustomerResponse.setAccountNumber(rejectCustomer.getAccountNumber());
        rejectCustomerResponse.setIfscCode(rejectCustomer.getIfscCode());
        rejectCustomerResponse.setBankName(rejectCustomer.getBankName());
        rejectCustomerResponse.setBranchName(rejectCustomer.getBranchName());
        rejectCustomerResponse.setBureauDetails(rejectCustomer.getBureauDetails());
        rejectCustomerResponse.setBureauType(rejectCustomer.getBureauType());
        rejectCustomerResponse.setBureauScore(rejectCustomer.getBureauScore());
        rejectCustomerResponse.setNewToCredit(rejectCustomer.getNewToCredit());
        rejectCustomerResponse.setOtherDetails(rejectCustomer.getOtherDetails());
        rejectCustomerResponse.setOtherDetails2(rejectCustomer.getOtherDetails2());
//        rejectCustomerResponse.setInsuranceCompany(rejectCustomer.getInsuranceCompany());
//        rejectCustomerResponse.setCoBorrowerName(rejectCustomer.getCoBorrowerName());
//        rejectCustomerResponse.setCoMobileNumber(rejectCustomer.getCoMobileNumber());
//        rejectCustomerResponse.setCoEmail(rejectCustomer.getCoEmail());
//        rejectCustomerResponse.setCoDob(rejectCustomer.getCoDob());
//        rejectCustomerResponse.setCoGender(rejectCustomer.getCoGender());
//        rejectCustomerResponse.setCoRelation(rejectCustomer.getCoRelation());
//        rejectCustomerResponse.setCoEmploymentType(rejectCustomer.getCoEmploymentType());
//        rejectCustomerResponse.setCoCompanyName(rejectCustomer.getCoCompanyName());
//        rejectCustomerResponse.setCoPanNumber(rejectCustomer.getCoPanNumber());
//        rejectCustomerResponse.setCoAadharNumber(rejectCustomer.getCoAadharNumber());
//        rejectCustomerResponse.setCoBankName(rejectCustomer.getCoBankName());
//        rejectCustomerResponse.setCoNameAsPerBank(rejectCustomer.getCoNameAsPerBank());
//        rejectCustomerResponse.setCoAccountNumber(rejectCustomer.getCoAccountNumber());
//        rejectCustomerResponse.setCoIfscCode(rejectCustomer.getCoIfscCode());
        rejectCustomerResponse.setCreatedDate(rejectCustomer.getCreatedDate());

        return rejectCustomerResponse;

    }

    public List<RejectCustomerEntity> getCustomersByStatus(String status) {
        return rejectCustomerRepository.findByStatus(status);
    }
    public long countTotalReject() {
        return rejectCustomerRepository.count();
    }





}
