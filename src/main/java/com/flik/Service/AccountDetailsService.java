package com.flik.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flik.apiresource.AccountNumberApiResource;
import com.flik.constant.AccountNumURLConstant;
import com.flik.entity.*;
import com.flik.model.AccountNumberModel;
import com.flik.model.CustomerVerifiedModel;
import com.flik.repository.*;
import com.flik.request.AccountNumRequest;
import com.flik.respons.CustomerVerifiedResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountDetailsService {



    @Autowired
    private AccountNumberApiResource accountNumberApiResource;

    @Autowired
    private CustomerAccountDetailsRepository customerAccountDetailsRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RejectCustomerRepository rejectCustomerRepository;

    @Autowired
    private CustomerVerifiedRepository customerVerifiedRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private LoanRepository loanRepository;



    public String accountDetails(AccountNumberModel accountNumberModel, Long customerId) throws JsonProcessingException {
        Optional<CustomerEntity> customer = customerRepository.findById(customerId);

        if (!customer.isPresent()) {
        return "Customer Id not found";
        }

    CustomerEntity customerOpt = customer.get();
    if (!"Kyc complete".equalsIgnoreCase(customerOpt.getStatus())) {
        return "Customer status is not 'Kyc complete'";
    }

    accountNumberModel.setBankAccountNumber(customerOpt.getAccountNumber());
    accountNumberModel.setBankIfscCode(customerOpt.getIfscCode());

    AccountNumRequest accountNumRequest = accountNumberApiResource.createRequestDto(accountNumberModel);
    String str = accountNumberApiResource.getAccountNumber(accountNumRequest, AccountNumURLConstant.account);

    AccountDetailsEntity accountDetails = customerAccountDetailsRepository.findByCustomerId(customerId)
            .orElse(new AccountDetailsEntity());
    accountDetails.setCustomerId(customerId);

    try {
        JSONObject jsonObject = new JSONObject(str);

        String bankAccountNumber = jsonObject.optString("bank_account_number", null);
        String nameAsPerBank = jsonObject.optString("name_as_per_bank", null);
        String bankIfscCode = jsonObject.optString("bank_ifsc_code", null);
        String status = jsonObject.optString("status", null);

        accountDetails.setAccountNumber(bankAccountNumber);
        accountDetails.setIfscCode(bankIfscCode);
        accountDetails.setCreatedDate(LocalDateTime.now());
        accountDetails.setStatus(status);
        customerAccountDetailsRepository.save(accountDetails);


        if ("success".equalsIgnoreCase(status) && nameAsPerBank != null && !nameAsPerBank.isEmpty()) {
            accountDetails.setNameOnBank(nameAsPerBank);
            customerOpt.setStatus("account verified");
            customerRepository.save(customerOpt);




        } else {
            customerOpt.setStatus("account failed");
            customerRepository.save(customerOpt);

            RejectCustomerEntity rejectCustomer = rejectCustomerRepository.findByCustomerId(customerId)
                    .orElse(new RejectCustomerEntity());

            rejectCustomer.setStatus("account failed");

            StringBuilder rejectionReason = new StringBuilder();
            if (nameAsPerBank == null || nameAsPerBank.isEmpty()) {
                rejectionReason.append("Name reject");
            }

            if (!"success".equalsIgnoreCase(status)) {
                if (rejectionReason.length() > 0) {
                    rejectionReason.append(" and ");
                }
                rejectionReason.append("Status reject");
            }

            rejectCustomer.setReason(rejectionReason.toString());
            rejectCustomer.setAccountName(nameAsPerBank);


            rejectCustomer.setAccountNumber(customerOpt.getAccountNumber());
            rejectCustomer.setIfscCode(customerOpt.getIfscCode());
            rejectCustomer.setCustomerId(customerId);

            rejectCustomer.setBankName(customerOpt.getBankName());
//            rejectCustomer.setBranchName(customerOpt.getBranchName());

            rejectCustomer.setPartnerLoanId(customerOpt.getPartnerLoanId());
            rejectCustomer.setFintechName(customerOpt.getFintechName());
//            rejectCustomer.setFintechId(customerOpt.getFintechId());
            rejectCustomer.setBorrowerName(customerOpt.getBorrowerName());
            rejectCustomer.setMobileNumber(customerOpt.getMobileNumber());
            rejectCustomer.setEmail(customerOpt.getEmail());
            rejectCustomer.setDob(customerOpt.getDob());
            rejectCustomer.setGender(customerOpt.getGender());
            rejectCustomer.setDobJoining(customerOpt.getDobJoining());
            rejectCustomer.setFatherName(customerOpt.getFatherName());
//            rejectCustomer.setMaritalStatus(customerOpt.getMaritalStatus());
//            rejectCustomer.setSpouseName(customerOpt.getSpouseName());
//            rejectCustomer.setNomineeName(customerOpt.getNomineeName());
//            rejectCustomer.setNomineeDob(customerOpt.getNomineeDob());
//            rejectCustomer.setNomineeGender(customerOpt.getNomineeGender());
//            rejectCustomer.setReferenceName(customerOpt.getReferenceName());
//            rejectCustomer.setReferenceMobileNumber(customerOpt.getReferenceMobileNumber());
//            rejectCustomer.setReferenceName2(customerOpt.getReferenceName2());
//            rejectCustomer.setReferenceMobileNumber2(customerOpt.getReferenceMobileNumber2());
//            rejectCustomer.setBranch(customerOpt.getBranch());
            rejectCustomer.setPanNumber(customerOpt.getPanNumber());
            rejectCustomer.setAadharNumber(customerOpt.getAadharNumber());
//            rejectCustomer.setResidentialType(customerOpt.getResidentialType());
//            rejectCustomer.setArea(customerOpt.getArea());
            rejectCustomer.setPinCode(customerOpt.getPinCode());
            rejectCustomer.setCity(customerOpt.getCity());
            rejectCustomer.setState(customerOpt.getState());
            rejectCustomer.setAddress(customerOpt.getAddress());
//            rejectCustomer.setLongitude(customerOpt.getLongitude());
//            rejectCustomer.setLatitude(customerOpt.getLatitude());
            rejectCustomer.setCompanyName(customerOpt.getCompanyName());
            rejectCustomer.setCompanyAddress(customerOpt.getCompanyAddress());
//            rejectCustomer.setProfessionalEmail(customerOpt.getProfessionalEmail());
//            rejectCustomer.setCompanyType(customerOpt.getCompanyType());
            rejectCustomer.setSalary(customerOpt.getSalary());
            rejectCustomer.setEmpId(customerOpt.getEmpId());
            rejectCustomer.setBureauDetails(customerOpt.getBureauDetails());
            rejectCustomer.setBureauType(customerOpt.getBureauType());
            rejectCustomer.setBureauScore(customerOpt.getBureauScore());
            rejectCustomer.setNewToCredit(customerOpt.getNewToCredit());
            rejectCustomer.setOtherDetails(customerOpt.getOtherDetails());
            rejectCustomer.setOtherDetails2(customerOpt.getOtherDetails2());
//            rejectCustomer.setInsuranceCompany(customerOpt.getInsuranceCompany());
//            rejectCustomer.setCoBorrowerName(customerOpt.getCoBorrowerName());
//            rejectCustomer.setCoMobileNumber(customerOpt.getCoMobileNumber());
//            rejectCustomer.setCoEmail(customerOpt.getCoEmail());
//            rejectCustomer.setCoDob(customerOpt.getCoDob());
//            rejectCustomer.setCoGender(customerOpt.getCoGender());
//            rejectCustomer.setCoRelation(customerOpt.getCoRelation());
//            rejectCustomer.setCoEmploymentType(customerOpt.getCoEmploymentType());
//            rejectCustomer.setCoCompanyName(customerOpt.getCoCompanyName());
//            rejectCustomer.setCoPanNumber(customerOpt.getCoPanNumber());
//            rejectCustomer.setCoAadharNumber(customerOpt.getCoAadharNumber());
//            rejectCustomer.setCoBankName(customerOpt.getCoBankName());
//            rejectCustomer.setCoNameAsPerBank(customerOpt.getCoNameAsPerBank());
//            rejectCustomer.setCoAccountNumber(customerOpt.getCoAccountNumber());
//            rejectCustomer.setCoIfscCode(customerOpt.getCoIfscCode());
            rejectCustomer.setCreatedDate(LocalDate.now());

            rejectCustomerRepository.save(rejectCustomer);

            return "Penny Drop process completed with rejection!";
        }

        return "Penny Drop process completed successfully!";
    } catch (Exception e) {
        e.printStackTrace();
        customerOpt.setStatus("account failed");
        customerRepository.save(customerOpt);
        return "Failed to save account details!";
    }
}
    public String transferToVerified(Long customerId) {
        Optional<CustomerEntity> optionalCustomer = customerRepository.findById(customerId);

        if (!optionalCustomer.isPresent()) {
            return "Customer with ID " + customerId + " not found.";
        }

        CustomerEntity customerEntity = optionalCustomer.get();

        // Check if the customer status is already 'Ready to disburse'
        Optional<CustomerVerified> existingVerified = customerVerifiedRepository.findByCustomerId(customerId);
        if (existingVerified.isPresent()) {
            return "Customer with ID " + customerId + " is already marked as 'Ready to disburse'.";
        }

        if (!"Account Verified".equalsIgnoreCase(customerEntity.getStatus())) {
            return "Customer with ID " + customerId + " is not 'Account Verified'.";
        }

         CustomerVerified customerVerified = new CustomerVerified();
        customerVerified.setCustomerId(customerEntity.getId());
        customerVerified.setPartnerLoanId(customerEntity.getPartnerLoanId());
        customerVerified.setFintechName(customerEntity.getFintechName());
//        customerVerified.setFintechId(customerEntity.getFintechId());
        customerVerified.setBorrowerName(customerEntity.getBorrowerName());
        customerVerified.setMobileNumber(customerEntity.getMobileNumber());
        customerVerified.setEmail(customerEntity.getEmail());
        customerVerified.setDob(customerEntity.getDob());
        customerVerified.setGender(customerEntity.getGender());
        customerVerified.setDobJoining(customerEntity.getDobJoining());
        customerVerified.setFatherName(customerEntity.getFatherName());
//        customerVerified.setMaritalStatus(customerEntity.getMaritalStatus());
//        customerVerified.setSpouseName(customerEntity.getSpouseName());
//        customerVerified.setNomineeName(customerEntity.getNomineeName());
//        customerVerified.setNomineeDob(customerEntity.getNomineeDob());
//        customerVerified.setNomineeGender(customerEntity.getNomineeGender());
//        customerVerified.setReferenceName(customerEntity.getReferenceName());
//        customerVerified.setReferenceMobileNumber(customerEntity.getReferenceMobileNumber());
//        customerVerified.setReferenceName2(customerEntity.getReferenceName2());
//        customerVerified.setReferenceMobileNumber2(customerEntity.getReferenceMobileNumber2());
//        customerVerified.setBranch(customerEntity.getBranch());
        customerVerified.setPanNumber(customerEntity.getPanNumber());
        customerVerified.setAadharNumber(customerEntity.getAadharNumber());
//        customerVerified.setResidentialType(customerEntity.getResidentialType());
//        customerVerified.setArea(customerEntity.getArea());
        customerVerified.setPinCode(customerEntity.getPinCode());
        customerVerified.setCity(customerEntity.getCity());
        customerVerified.setState(customerEntity.getState());
        customerVerified.setAddress(customerEntity.getAddress());
//        customerVerified.setLongitude(customerEntity.getLongitude());
//        customerVerified.setLatitude(customerEntity.getLatitude());
        customerVerified.setCompanyName(customerEntity.getCompanyName());
        customerVerified.setCompanyAddress(customerEntity.getCompanyAddress());
//        customerVerified.setProfessionalEmail(customerEntity.getProfessionalEmail());
//        customerVerified.setCompanyType(customerEntity.getCompanyType());
        customerVerified.setSalary(customerEntity.getSalary());
        customerVerified.setEmpId(customerEntity.getEmpId());
        customerVerified.setAccountName(customerEntity.getAccountName());
        customerVerified.setAccountNumber(customerEntity.getAccountNumber());
        customerVerified.setIfscCode(customerEntity.getIfscCode());
        customerVerified.setBankName(customerEntity.getBankName());
//        customerVerified.setBranchName(customerEntity.getBranchName());
        customerVerified.setBureauDetails(customerEntity.getBureauDetails());
        customerVerified.setBureauType(customerEntity.getBureauType());
        customerVerified.setBureauScore(customerEntity.getBureauScore());
        customerVerified.setNewToCredit(customerEntity.getNewToCredit());
        customerVerified.setOtherDetails(customerEntity.getOtherDetails());
        customerVerified.setOtherDetails2(customerEntity.getOtherDetails2());
//        customerVerified.setInsuranceCompany(customerEntity.getInsuranceCompany());
//        customerVerified.setCoBorrowerName(customerEntity.getCoBorrowerName());
//        customerVerified.setCoMobileNumber(customerEntity.getCoMobileNumber());
//        customerVerified.setCoEmail(customerEntity.getCoEmail());
//        customerVerified.setCoDob(customerEntity.getCoDob());
//        customerVerified.setCoGender(customerEntity.getCoGender());
//        customerVerified.setCoRelation(customerEntity.getCoRelation());
//        customerVerified.setCoEmploymentType(customerEntity.getCoEmploymentType());
//        customerVerified.setCoCompanyName(customerEntity.getCoCompanyName());
//        customerVerified.setCoPanNumber(customerEntity.getCoPanNumber());
//        customerVerified.setCoAadharNumber(customerEntity.getCoAadharNumber());
//        customerVerified.setCoBankName(customerEntity.getCoBankName());
//        customerVerified.setCoNameAsPerBank(customerEntity.getCoNameAsPerBank());
//        customerVerified.setCoAccountNumber(customerEntity.getCoAccountNumber());
//        customerVerified.setCoIfscCode(customerEntity.getCoIfscCode());
        customerVerified.setCreatedDate(customerEntity.getCreatedDate());
        customerVerified.setStatus("Ready to disbursed");

        customerVerifiedRepository.save(customerVerified);

        // Update the customer status
        customerEntity.setStatus("Ready to disbursed");
        customerRepository.save(customerEntity);

        return "Customer with ID " + customerId + " successfully transferred to 'Ready to disburse'.";
    }






    public String getIfscDetails(String ifsccode , Long customerId) {


        Optional<CustomerEntity> customeropt = customerRepository.findById(customerId);

        if (!customeropt.isPresent()) {
            return "Customer not found";
        }
        CustomerEntity customer = customeropt.get();

        String str1 = accountNumberApiResource.ifscCode(ifsccode);
        ObjectMapper objectMapper = new ObjectMapper();


        AccountDetailsEntity accountDetails = customerAccountDetailsRepository.findByCustomerId(customerId)
                .orElse(new AccountDetailsEntity());

        accountDetails.setCustomerId(customerId);
        try {
            JsonNode jsonNode = objectMapper.readTree(str1);
            JsonNode list = jsonNode.findValue("data");
            JSONArray j1 = new JSONArray(list.toString());
            j1.length();
            JSONObject jobject = j1.getJSONObject(0);

            if (jobject.getString("ifsc_code") != null) {
                String ifsc_code = jobject.getString("ifsc_code");
                String branch = jobject.getString("branch");
                String bank = jobject.getString("bank");


                accountDetails.setBranchName(branch);
                accountDetails.setBankName(bank);



                customerAccountDetailsRepository.save(accountDetails);

                customer.setBankName(bank);
//                customer.setBranchName(branch);
                customerRepository.save(customer);

                return "Account details saved successfully!";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to save account details!";
        }

        return "IFSC code not found in response.";
    }
    public List<CustomerVerified> verifiedCustomerList() {

        return customerVerifiedRepository.findAll();
    }
    public CustomerVerifiedResponse getVerifiedCustomerById(Long customerId) {
        CustomerVerified customerVerified = customerVerifiedRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Verified Customer not found with ID: " + customerId));

        CustomerVerifiedResponse customerVerifiedResponse= new CustomerVerifiedResponse();

        customerVerifiedResponse.setId(customerVerified.getId());
        customerVerifiedResponse.setCustomerId(customerVerified.getCustomerId());
        customerVerifiedResponse.setStatus(customerVerified.getStatus());
        customerVerifiedResponse.setCustomerId(customerVerified.getCustomerId());
      //  customerVerifiedResponse.setReason(rejectCustomer.getReason());
        customerVerifiedResponse.setPartnerLoanId(customerVerified.getPartnerLoanId());
        customerVerifiedResponse.setFintechName(customerVerified.getFintechName());
        customerVerifiedResponse.setFintechId(customerVerified.getFintechId());
        customerVerifiedResponse.setBorrowerName(customerVerified.getBorrowerName());
        customerVerifiedResponse.setMobileNumber(customerVerified.getMobileNumber());
        customerVerifiedResponse.setEmail(customerVerified.getEmail());
        customerVerifiedResponse.setDob(customerVerified.getDob());
        customerVerifiedResponse.setGender(customerVerified.getGender());
        customerVerifiedResponse.setDobJoining(customerVerified.getDobJoining());
        customerVerifiedResponse.setFatherName(customerVerified.getFatherName());
//        customerVerifiedResponse.setMaritalStatus(customerVerified.getMaritalStatus());
//        customerVerifiedResponse.setSpouseName(customerVerified.getSpouseName());
//        customerVerifiedResponse.setNomineeName(customerVerified.getNomineeName());
//        customerVerifiedResponse.setNomineeDob(customerVerified.getNomineeDob());
//        customerVerifiedResponse.setNomineeGender(customerVerified.getNomineeGender());
//        customerVerifiedResponse.setReferenceName(customerVerified.getReferenceName());
//        customerVerifiedResponse.setReferenceMobileNumber(customerVerified.getReferenceMobileNumber());
//        customerVerifiedResponse.setReferenceName2(customerVerified.getReferenceName2());
//        customerVerifiedResponse.setReferenceMobileNumber2(customerVerified.getReferenceMobileNumber2());
//        customerVerifiedResponse.setBranch(customerVerified.getBranch());
        customerVerifiedResponse.setPanNumber(customerVerified.getPanNumber());
        customerVerifiedResponse.setAadharNumber(customerVerified.getAadharNumber());
        customerVerifiedResponse.setResidentialType(customerVerified.getResidentialType());
        customerVerifiedResponse.setPinCode(customerVerified.getPinCode());
        customerVerifiedResponse.setCity(customerVerified.getCity());
        customerVerifiedResponse.setState(customerVerified.getState());
        customerVerifiedResponse.setAddress(customerVerified.getAddress());
//        customerVerifiedResponse.setLongitude(customerVerified.getLongitude());
//        customerVerifiedResponse.setLatitude(customerVerified.getLatitude());
        customerVerifiedResponse.setCompanyName(customerVerified.getCompanyName());
//        customerVerifiedResponse.setPosition(customerVerified.getPosition());
        customerVerifiedResponse.setCompanyAddress(customerVerified.getCompanyAddress());
        customerVerifiedResponse.setSalary(customerVerified.getSalary());
        customerVerifiedResponse.setEmpId(customerVerified.getEmpId());
        customerVerifiedResponse.setAccountName(customerVerified.getAccountName());
        customerVerifiedResponse.setAccountNumber(customerVerified.getAccountNumber());
        customerVerifiedResponse.setIfscCode(customerVerified.getIfscCode());
        customerVerifiedResponse.setBankName(customerVerified.getBankName());
        customerVerifiedResponse.setBranchName(customerVerified.getBranchName());
        customerVerifiedResponse.setBureauDetails(customerVerified.getBureauDetails());
        customerVerifiedResponse.setBureauType(customerVerified.getBureauType());
        customerVerifiedResponse.setBureauScore(customerVerified.getBureauScore());
        customerVerifiedResponse.setNewToCredit(customerVerified.getNewToCredit());
        customerVerifiedResponse.setOtherDetails(customerVerified.getOtherDetails());
        customerVerifiedResponse.setOtherDetails2(customerVerified.getOtherDetails2());
//        customerVerifiedResponse.setInsuranceCompany(customerVerified.getInsuranceCompany());
//        customerVerifiedResponse.setCoBorrowerName(customerVerified.getCoBorrowerName());
//        customerVerifiedResponse.setCoMobileNumber(customerVerified.getCoMobileNumber());
//        customerVerifiedResponse.setCoEmail(customerVerified.getCoEmail());
//        customerVerifiedResponse.setCoDob(customerVerified.getCoDob());
//        customerVerifiedResponse.setCoGender(customerVerified.getCoGender());
//        customerVerifiedResponse.setCoRelation(customerVerified.getCoRelation());
//        customerVerifiedResponse.setCoEmploymentType(customerVerified.getCoEmploymentType());
//        customerVerifiedResponse.setCoCompanyName(customerVerified.getCoCompanyName());
//        customerVerifiedResponse.setCoPanNumber(customerVerified.getCoPanNumber());
//        customerVerifiedResponse.setCoAadharNumber(customerVerified.getCoAadharNumber());
//        customerVerifiedResponse.setCoBankName(customerVerified.getCoBankName());
//        customerVerifiedResponse.setCoNameAsPerBank(customerVerified.getCoNameAsPerBank());
//        customerVerifiedResponse.setCoAccountNumber(customerVerified.getCoAccountNumber());
//        customerVerifiedResponse.setCoIfscCode(customerVerified.getCoIfscCode());
        customerVerifiedResponse.setCreatedDate(customerVerified.getCreatedDate());
        customerVerifiedResponse.setTvrReason(customerVerified.getTvrReason());
        customerVerifiedResponse.setOperationReason(customerVerified.getOperationReason());
        customerVerifiedResponse.setCreditReason(customerVerified.getCreditReason());


        return customerVerifiedResponse;

    }
    public long countActiveBorrowers() {
        return customerVerifiedRepository.countByStatus("Verified Customers");
    }

    public long countClosedAccounts() {
        return customerVerifiedRepository.countByStatus("AccountClosed");
    }






    public byte[] exportAllCustomerDetailsToExcel(List<CustomerVerified> customerDetails) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet(" Verified Customer Details");
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "ID", "Customer ID", "Partner Loan ID", "Fintech Name", "Fintech ID", "Borrower Name",
                "Mobile Number", "Email", "Date Of Birth", "Gender", "PAN Number", "Aadhar Number","Pin Code", "City", "State", "Address",
                "Company Name", "Company Address",  "Salary",
                "Account Name", "Account Number", "IFSC Code", "Bank Name",  "Bureau Details",
                "Bureau Type", "Bureau Score", "New To Credit", "Other Details", "Other Details 2",
                "Status", "Created Date" ,"Credit Reason", "Tvr Reason", "Operation Reason"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (CustomerVerified customer : customerDetails) {
            Row dataRow = sheet.createRow(rowNum++);

            dataRow.createCell(0).setCellValue(customer.getId());
            dataRow.createCell(1).setCellValue(customer.getCustomerId());
            dataRow.createCell(2).setCellValue(customer.getPartnerLoanId());
            dataRow.createCell(3).setCellValue(customer.getFintechName());
            dataRow.createCell(4).setCellValue(customer.getFintechId());
            dataRow.createCell(5).setCellValue(customer.getBorrowerName());
            dataRow.createCell(6).setCellValue(customer.getMobileNumber());
            dataRow.createCell(7).setCellValue(customer.getEmail());
            dataRow.createCell(8).setCellValue(customer.getDob());
            dataRow.createCell(9).setCellValue(customer.getGender());
            dataRow.createCell(8).setCellValue(customer.getDobJoining());
            dataRow.createCell(9).setCellValue(customer.getFatherName());
//            dataRow.createCell(10).setCellValue(customer.getMaritalStatus());
//            dataRow.createCell(11).setCellValue(customer.getSpouseName());
//            dataRow.createCell(12).setCellValue(customer.getNomineeName());
//            dataRow.createCell(13).setCellValue(customer.getNomineeDob());
//            dataRow.createCell(14).setCellValue(customer.getNomineeGender());
//            dataRow.createCell(15).setCellValue(customer.getReferenceName());
//            dataRow.createCell(16).setCellValue(customer.getReferenceMobileNumber());
//            dataRow.createCell(17).setCellValue(customer.getReferenceName2());
//            dataRow.createCell(18).setCellValue(customer.getReferenceMobileNumber2());
//            dataRow.createCell(19).setCellValue(customer.getBranch());
            dataRow.createCell(20).setCellValue(customer.getPanNumber());
            dataRow.createCell(21).setCellValue(customer.getAadharNumber());
//            dataRow.createCell(22).setCellValue(customer.getResidentialType());
//            dataRow.createCell(23).setCellValue(customer.getArea());
            dataRow.createCell(24).setCellValue(customer.getPinCode());
            dataRow.createCell(25).setCellValue(customer.getCity());
            dataRow.createCell(26).setCellValue(customer.getState());
            dataRow.createCell(27).setCellValue(customer.getAddress());
//            dataRow.createCell(28).setCellValue(customer.getLongitude());
//            dataRow.createCell(29).setCellValue(customer.getLatitude());
            dataRow.createCell(30).setCellValue(customer.getCompanyName());
//            dataRow.createCell(31).setCellValue(customer.getPosition());
            dataRow.createCell(32).setCellValue(customer.getCompanyAddress());
            dataRow.createCell(33).setCellValue(customer.getDobJoining());
            dataRow.createCell(34).setCellValue(customer.getFatherName());
            dataRow.createCell(35).setCellValue(customer.getSalary());
            dataRow.createCell(35).setCellValue(customer.getEmpId());
            dataRow.createCell(36).setCellValue(customer.getAccountName());
            dataRow.createCell(37).setCellValue(customer.getAccountNumber());
            dataRow.createCell(38).setCellValue(customer.getIfscCode());
            dataRow.createCell(39).setCellValue(customer.getBankName());
            dataRow.createCell(40).setCellValue(customer.getBranchName());
            dataRow.createCell(41).setCellValue(customer.getBureauDetails());
            dataRow.createCell(42).setCellValue(customer.getBureauType());
            dataRow.createCell(43).setCellValue(customer.getBureauScore());
            dataRow.createCell(44).setCellValue(customer.getNewToCredit());
            dataRow.createCell(45).setCellValue(customer.getOtherDetails());
            dataRow.createCell(46).setCellValue(customer.getOtherDetails2());
//            dataRow.createCell(47).setCellValue(customer.getInsuranceCompany());
//            dataRow.createCell(48).setCellValue(customer.getCoBorrowerName());
//            dataRow.createCell(49).setCellValue(customer.getCoMobileNumber());
//            dataRow.createCell(50).setCellValue(customer.getCoEmail());
//            dataRow.createCell(51).setCellValue(customer.getCoDob());
//            dataRow.createCell(52).setCellValue(customer.getCoGender());
//            dataRow.createCell(53).setCellValue(customer.getCoRelation());
//            dataRow.createCell(54).setCellValue(customer.getCoEmploymentType());
//            dataRow.createCell(55).setCellValue(customer.getCoCompanyName());
//            dataRow.createCell(56).setCellValue(customer.getCoPanNumber());
//            dataRow.createCell(57).setCellValue(customer.getCoAadharNumber());
//            dataRow.createCell(58).setCellValue(customer.getCoBankName());
//            dataRow.createCell(59).setCellValue(customer.getCoNameAsPerBank());
//            dataRow.createCell(60).setCellValue(customer.getCoAccountNumber());
//            dataRow.createCell(61).setCellValue(customer.getCoIfscCode());
            dataRow.createCell(62).setCellValue(customer.getStatus());
            dataRow.createCell(63).setCellValue(customer.getCreatedDate().toString());
            dataRow.createCell(64).setCellValue(customer.getTvrReason());
            dataRow.createCell(65).setCellValue(customer.getOperationReason());
            dataRow.createCell(66).setCellValue(customer.getCreditReason());

        }

        workbook.write(byteArrayOutputStream);
        workbook.close();
        return byteArrayOutputStream.toByteArray();
    }


    public String deleteVerifiedCustomerByCustomerId(Long customerId, String providedPassword) {

     Optional<AdminEntity> adminOptional = adminRepository.findByAdminId("Delete");
    if (adminOptional.isEmpty()) {
        return "Admin with ID 'Delete' not found.";
    }

    AdminEntity admin = adminOptional.get();

     if (!admin.getAdminPassword().equals(providedPassword)) {
        return "Invalid password. Customer deletion failed.";
    }

     Optional<CustomerVerified> customerOptional = customerVerifiedRepository.findByCustomerId(customerId);
    if (customerOptional.isEmpty()) {
        return "Customer with ID " + customerId + " not found.";
    }

    customerVerifiedRepository.deleteById(customerOptional.get().getId());


    if (customerVerifiedRepository.findByCustomerId(customerId).isPresent()) {
        return "Customer deletion failed.";
    }

    return "Customer deleted successfully.";
}


    public String statusDisbursed(Long customerId) {
        Optional<CustomerVerified> customerOpt = customerVerifiedRepository.findByCustomerId(customerId);
        if (customerOpt.isPresent()) {
            CustomerVerified customer = customerOpt.get();
            if ("Ready To Disbursed".equalsIgnoreCase(customer.getStatus())) {
                customer.setStatus("Disbursed");
                customerVerifiedRepository.save(customer);
                return "Disbursed completed successfully.";
            } else {
                return "Customer is not in  Ready To Disbursed status.";
            }
        } else {
            return "Customer not found.";
        }
    }

    public void updateEmployeePersonalDetails(Long customerId, CustomerVerifiedModel customerVerifiedResponse) {
        Optional<CustomerVerified> optionalCustomerVerified = customerVerifiedRepository.findByCustomerId(customerId);

        if (optionalCustomerVerified.isPresent()) {
            CustomerVerified customerVerified = optionalCustomerVerified.get();


            customerVerified.setPartnerLoanId(customerVerifiedResponse.getPartnerLoanId());
            customerVerified.setFintechName(customerVerifiedResponse.getFintechName());
            customerVerified.setFintechId(customerVerifiedResponse.getFintechId());
            customerVerified.setBorrowerName(customerVerifiedResponse.getBorrowerName());
            customerVerified.setMobileNumber(customerVerifiedResponse.getMobileNumber());
            customerVerified.setEmail(customerVerifiedResponse.getEmail());
            customerVerified.setDob(customerVerifiedResponse.getDob());
            customerVerified.setGender(customerVerifiedResponse.getGender());
            customerVerified.setDobJoining(customerVerifiedResponse.getDobJoining());
            customerVerified.setFatherName(customerVerifiedResponse.getFatherName());
//            customerVerified.setMaritalStatus(customerVerifiedResponse.getMaritalStatus());
//            customerVerified.setSpouseName(customerVerifiedResponse.getSpouseName());
//            customerVerified.setNomineeName(customerVerifiedResponse.getNomineeName());
//            customerVerified.setNomineeDob(customerVerifiedResponse.getNomineeDob());
//            customerVerified.setNomineeGender(customerVerifiedResponse.getNomineeGender());
//            customerVerified.setReferenceName(customerVerifiedResponse.getReferenceName());
//            customerVerified.setReferenceMobileNumber(customerVerifiedResponse.getReferenceMobileNumber());
//            customerVerified.setReferenceName2(customerVerifiedResponse.getReferenceName2());
//            customerVerified.setReferenceMobileNumber2(customerVerifiedResponse.getReferenceMobileNumber2());
//            customerVerified.setBranch(customerVerifiedResponse.getBranch());
            customerVerified.setPanNumber(customerVerifiedResponse.getPanNumber());
            customerVerified.setAadharNumber(customerVerifiedResponse.getAadharNumber());
//            customerVerified.setDobJoining(customerVerifiedResponse.getDobJoining());
//            customerVerified.setFatherName(customerVerifiedResponse.getFatherName());
            customerVerified.setPinCode(customerVerifiedResponse.getPinCode());
            customerVerified.setCity(customerVerifiedResponse.getCity());
            customerVerified.setState(customerVerifiedResponse.getState());
            customerVerified.setAddress(customerVerifiedResponse.getAddress());
//            customerVerified.setLongitude(customerVerifiedResponse.getLongitude());
//            customerVerified.setLatitude(customerVerifiedResponse.getLatitude());
            customerVerified.setCompanyName(customerVerifiedResponse.getCompanyName());
//            customerVerified.setPosition(customerVerifiedResponse.getPosition());
            customerVerified.setCompanyAddress(customerVerifiedResponse.getCompanyAddress());
//            customerVerified.setProfessionalEmail(customerVerifiedResponse.getProfessionalEmail());
//            customerVerified.setCompanyType(customerVerifiedResponse.getCompanyType());
            customerVerified.setSalary(customerVerifiedResponse.getSalary());
            customerVerified.setEmpId(customerVerifiedResponse.getEmpId());
            customerVerified.setAccountName(customerVerifiedResponse.getAccountName());
            customerVerified.setAccountNumber(customerVerifiedResponse.getAccountNumber());
            customerVerified.setIfscCode(customerVerifiedResponse.getIfscCode());
            customerVerified.setBankName(customerVerifiedResponse.getBankName());
            customerVerified.setBranchName(customerVerifiedResponse.getBranchName());
            customerVerified.setBureauDetails(customerVerifiedResponse.getBureauDetails());
            customerVerified.setBureauType(customerVerifiedResponse.getBureauType());
            customerVerified.setBureauScore(customerVerifiedResponse.getBureauScore());
            customerVerified.setNewToCredit(customerVerifiedResponse.getNewToCredit());
            customerVerified.setOtherDetails(customerVerifiedResponse.getOtherDetails());
            customerVerified.setOtherDetails2(customerVerifiedResponse.getOtherDetails2());
//            customerVerified.setInsuranceCompany(customerVerifiedResponse.getInsuranceCompany());
//            customerVerified.setCoBorrowerName(customerVerifiedResponse.getCoBorrowerName());
//            customerVerified.setCoMobileNumber(customerVerifiedResponse.getCoMobileNumber());
//            customerVerified.setCoEmail(customerVerifiedResponse.getCoEmail());
//            customerVerified.setCoDob(customerVerifiedResponse.getCoDob());
//            customerVerified.setCoGender(customerVerifiedResponse.getCoGender());
//            customerVerified.setCoRelation(customerVerifiedResponse.getCoRelation());
//            customerVerified.setCoEmploymentType(customerVerifiedResponse.getCoEmploymentType());
//            customerVerified.setCoCompanyName(customerVerifiedResponse.getCoCompanyName());
//            customerVerified.setCoPanNumber(customerVerifiedResponse.getCoPanNumber());
//            customerVerified.setCoAadharNumber(customerVerifiedResponse.getCoAadharNumber());
//            customerVerified.setCoBankName(customerVerifiedResponse.getCoBankName());
//            customerVerified.setCoNameAsPerBank(customerVerifiedResponse.getCoNameAsPerBank());
//            customerVerified.setCoAccountNumber(customerVerifiedResponse.getCoAccountNumber());
//            customerVerified.setCoIfscCode(customerVerifiedResponse.getCoIfscCode());
            customerVerified.setCreatedDate(customerVerifiedResponse.getCreatedDate());
            customerVerified.setStatus(customerVerifiedResponse.getStatus());
            customerVerified.setCreditReason(customerVerifiedResponse.getCreditReason());
            customerVerified.setTvrReason(customerVerifiedResponse.getTvrReason());
            customerVerified.setOperationReason(customerVerifiedResponse.getOperationReason());

            // Save the updated entity
            customerVerifiedRepository.save(customerVerified);
        } else {
            throw new EntityNotFoundException("CustomerVerified not found with id: " + customerId);
        }
    }

    public List<CustomerVerified> getNewCustomersByStatus(String status) {
        return customerVerifiedRepository.findByStatus(status);
    }

    public List<CustomerVerified> verifiedSearchByBorrowerName(String borrowerName) {
        return customerVerifiedRepository.findByBorrowerName(borrowerName);
    }

    public List<CustomerVerified> verifiedSearchByMobileNumber(String mobileNumber) {
        return customerVerifiedRepository.findByMobileNumber(mobileNumber);
    }


//
//    @Transactional
//    public void verifyCustomer(Long loanId) {
//        // Find the loan by its ID
//        LoanEntity loan = loanRepository.findById(loanId)
//                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
//
//        // Calculate 4% processing fee
//        double processingFee = loan.getLoanAmount() * 0.04;
//
//        // Deduct the processing fee from the loan amount
//        double updatedLoanAmount = loan.getLoanAmount() - processingFee;
//        loan.setLoanAmount(updatedLoanAmount);
//        loan.setProcessingFee(String.valueOf(processingFee));
//
//        // Save the updated loan entity
//        loanRepository.save(loan);
//
//        // Populate CustomerVerified with relevant information from LoanEntity
//        CustomerVerified customerVerified = new CustomerVerified();
//        customerVerified.setCustomerId(loan.getId()); // Assuming this maps to customerId
//        customerVerified.setPartnerLoanId(loan.getPartnerLoanId());
//        customerVerified.setBorrowerName(loan.getBorrowerName());
//        customerVerified.setMobileNumber(loan.getMobileNumber());
//
//        customerVerified.setStatus(loan.getStatus());
//
//
//
//        customerVerified.setCreatedDate(LocalDate.now());
//
//        // Emi Amount (using repayment field from LoanEntity as per your instruction)
//
//
//        // Save the verified customer information
//        customerVerifiedRepository.save(customerVerified);
//    }
//
//



}