package com.flik.Service;

import com.flik.entity.CustomerEntity;
import com.flik.model.CustomerRequestModel;
import com.flik.repository.CustomerRepository;
import com.flik.repository.UserRepository;
import com.flik.request.CustomerRequest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CustomerServices {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;

    public void save(MultipartFile file) {
        try {
            List<CustomerEntity> customers = saveExcelFile(file.getInputStream());
//            if (customers.size() < 1 || customers.size() > 10) {
//                throw new IllegalArgumentException("Excel file must contain between 1 and 10 rows.");
//            }
            customerRepository.saveAll(customers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    private List<CustomerEntity> saveExcelFile(InputStream inputStream) throws IOException {
        List<CustomerEntity> customers = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            CustomerEntity customer = new CustomerEntity();

            customer.setPartnerLoanId(trimToMaxLength(getCellValueAsString(row.getCell(1)), 255)); // Partner Loan ID
            customer.setFintechName(trimToMaxLength(getCellValueAsString(row.getCell(2)), 255)); // Fintech Partner Name
            customer.setBorrowerName(trimToMaxLength(getCellValueAsString(row.getCell(3)), 255)); // Borrower Name
            customer.setMobileNumber(trimToMaxLength(getCellValueAsString(row.getCell(4)), 255)); // Mobile Number
            customer.setEmail(trimToMaxLength(getCellValueAsString(row.getCell(5)), 255)); // Email
            customer.setDob(trimToMaxLength(getCellValueAsString(row.getCell(6)), 255)); // DOB
            customer.setGender(trimToMaxLength(getCellValueAsString(row.getCell(7)), 255)); // Gender
//            customer.setMaritalStatus(trimToMaxLength(getCellValueAsString(row.getCell(8)), 255)); // Marital Status
//            customer.setSpouseName(trimToMaxLength(getCellValueAsString(row.getCell(9)), 255)); // Spouse Name
//            customer.setNomineeName(trimToMaxLength(getCellValueAsString(row.getCell(10)), 255)); // Nominee Name
//            customer.setNomineeDob(trimToMaxLength(getCellValueAsString(row.getCell(11)), 255)); // Nominee DOB
//            customer.setNomineeGender(trimToMaxLength(getCellValueAsString(row.getCell(12)), 255)); // Nominee Gender
//            customer.setReferenceName(trimToMaxLength(getCellValueAsString(row.getCell(13)), 255)); // Reference Name
//            customer.setReferenceMobileNumber(trimToMaxLength(getCellValueAsString(row.getCell(14)), 255)); // Reference Number
//            customer.setReferenceName2(trimToMaxLength(getCellValueAsString(row.getCell(15)), 255)); // Reference Name 2
//            customer.setReferenceMobileNumber2(trimToMaxLength(getCellValueAsString(row.getCell(16)), 255)); // Reference Number 2
//            customer.setBranch(trimToMaxLength(getCellValueAsString(row.getCell(17)), 255)); // Branch
            customer.setPanNumber(trimToMaxLength(getCellValueAsString(row.getCell(18)), 255)); // Pan Number
            customer.setAadharNumber(trimToMaxLength(getCellValueAsString(row.getCell(19)), 255)); // Aadhar Number
//            customer.setResidentialType(trimToMaxLength(getCellValueAsString(row.getCell(20)), 255)); // Residential Type
//            customer.setArea(trimToMaxLength(getCellValueAsString(row.getCell(21)), 255)); // Area
            customer.setPinCode(trimToMaxLength(getCellValueAsString(row.getCell(22)), 255)); // Pincode
            customer.setCity(trimToMaxLength(getCellValueAsString(row.getCell(23)), 255)); // City
            customer.setState(trimToMaxLength(getCellValueAsString(row.getCell(24)), 255)); // State
            customer.setAddress(trimToMaxLength(getCellValueAsString(row.getCell(25)), 255)); // Address
//            customer.setLongitude(trimToMaxLength(getCellValueAsString(row.getCell(26)), 255)); // Longitude
//            customer.setLatitude(trimToMaxLength(getCellValueAsString(row.getCell(27)), 255)); // Latitude
            customer.setCompanyName(trimToMaxLength(getCellValueAsString(row.getCell(28)), 255)); // Company Name
            customer.setDobJoining(trimToMaxLength(getCellValueAsString(row.getCell(29)), 255)); // Position
            customer.setCompanyAddress(trimToMaxLength(getCellValueAsString(row.getCell(30)), 255)); // Company Address
//            customer.setProfessionalEmail(trimToMaxLength(getCellValueAsString(row.getCell(31)), 255)); // Professional Email
//            customer.setCompanyType(trimToMaxLength(getCellValueAsString(row.getCell(32)), 255)); // Company Type
            customer.setSalary(trimToMaxLength(getCellValueAsString(row.getCell(33)), 255)); // Salary/Income
            customer.setEmpId(trimToMaxLength(getCellValueAsString(row.getCell(33)), 255)); // Salary/Income
            customer.setAccountName(trimToMaxLength(getCellValueAsString(row.getCell(34)), 255)); // Account Name
            customer.setAccountNumber(trimToMaxLength(getCellValueAsString(row.getCell(35)), 255)); // Account Number
            customer.setIfscCode(trimToMaxLength(getCellValueAsString(row.getCell(36)), 255)); // IFSC Code
            customer.setBankName(trimToMaxLength(getCellValueAsString(row.getCell(37)), 255)); // Bank Name
//            customer.setBranchName(trimToMaxLength(getCellValueAsString(row.getCell(38)), 255)); // Branch Name
            customer.setBureauDetails(trimToMaxLength(getCellValueAsString(row.getCell(39)), 255));
            customer.setBureauType(trimToMaxLength(getCellValueAsString(row.getCell(40)), 255)); // Bureau Type
            customer.setBureauScore(trimToMaxLength(getCellValueAsString(row.getCell(41)), 255)); // Bureau Score
            customer.setNewToCredit(trimToMaxLength(getCellValueAsString(row.getCell(42)), 255)); // New To Credit
            customer.setOtherDetails(trimToMaxLength(getCellValueAsString(row.getCell(43)), 255)); // Other Details
            customer.setOtherDetails2(trimToMaxLength(getCellValueAsString(row.getCell(44)), 255)); // Other Details 2
//            customer.setInsuranceCompany(trimToMaxLength(getCellValueAsString(row.getCell(45)), 255)); // Insurance Company
//            customer.setCoBorrowerName(trimToMaxLength(getCellValueAsString(row.getCell(46)), 255)); // Co-Borrower Name
//            customer.setCoMobileNumber(trimToMaxLength(getCellValueAsString(row.getCell(47)), 255)); // Co-Mobile Number
//            customer.setCoEmail(trimToMaxLength(getCellValueAsString(row.getCell(48)), 255)); // Co-Email
//            customer.setCoDob(trimToMaxLength(getCellValueAsString(row.getCell(49)), 255)); // Co-DOB
//            customer.setCoGender(trimToMaxLength(getCellValueAsString(row.getCell(50)), 255)); // Co-Gender
//            customer.setCoRelation(trimToMaxLength(getCellValueAsString(row.getCell(51)), 255)); // Co-Relation
//            customer.setCoEmploymentType(trimToMaxLength(getCellValueAsString(row.getCell(52)), 255)); // Co-Employment Type
//            customer.setCoCompanyName(trimToMaxLength(getCellValueAsString(row.getCell(53)), 255)); // Co-Company Name
//            customer.setCoPanNumber(trimToMaxLength(getCellValueAsString(row.getCell(54)), 255)); // Co-Pan Number
//            customer.setCoAadharNumber(trimToMaxLength(getCellValueAsString(row.getCell(55)), 255)); // Co-Aadhar Number
//            customer.setCoNameAsPerBank(trimToMaxLength(getCellValueAsString(row.getCell(56)), 255)); // Co-Name As Per Bank
//            customer.setCoAccountNumber(trimToMaxLength(getCellValueAsString(row.getCell(57)), 255)); // Co-Account Number
//            customer.setCoIfscCode(trimToMaxLength(getCellValueAsString(row.getCell(58)), 255)); // Co-IFSC Code
//            customer.setCoBankName(trimToMaxLength(getCellValueAsString(row.getCell(59)), 255)); // Co-Bank Name
            customer.setCreatedDate(LocalDate.now());
            customer.setStatus(trimToMaxLength(getCellValueAsString(row.getCell(60)),255));
//            customer.setFintechId(trimToMaxLength(getCellValueAsString(row.getCell(61)),255));
            customers.add(customer);
        }


        workbook.close();
        customerRepository.saveAll(customers);
        return customers;
    }


    private String trimToMaxLength(String value, int maxLength) {
        if (value != null && value.length() > maxLength) {
            return value.substring(0, maxLength);
        }
        return value;
    }

    // Method to read the Excel file and return the list of customers for preview
    public List<CustomerEntity> previewCustomers(MultipartFile file) throws IOException {
        return readExcelFile(file.getInputStream());
    }


    private List<CustomerEntity> readExcelFile(InputStream inputStream) throws IOException {
        List<CustomerEntity> customers = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            CustomerEntity customer = new CustomerEntity();
            // Set customer fields based on the columns
            customer.setPartnerLoanId(trimToMaxLength(getCellValueAsString(row.getCell(1)), 255)); // Partner Loan ID
            customer.setFintechName(trimToMaxLength(getCellValueAsString(row.getCell(2)), 255)); // Fintech Partner Name
            customer.setBorrowerName(trimToMaxLength(getCellValueAsString(row.getCell(3)), 255)); // Borrower Name
            customer.setMobileNumber(trimToMaxLength(getCellValueAsString(row.getCell(4)), 255)); // Mobile Number
            customer.setEmail(trimToMaxLength(getCellValueAsString(row.getCell(5)), 255)); // Email
            customer.setDob(trimToMaxLength(getCellValueAsString(row.getCell(6)), 255)); // DOB
            customer.setGender(trimToMaxLength(getCellValueAsString(row.getCell(7)), 255)); // Gender
            customer.setDobJoining(trimToMaxLength(getCellValueAsString(row.getCell(7)), 255)); // Gender
            customer.setFatherName(trimToMaxLength(getCellValueAsString(row.getCell(7)), 255)); // Gender

//            customer.setMaritalStatus(trimToMaxLength(getCellValueAsString(row.getCell(8)), 255)); // Marital Status
//            customer.setSpouseName(trimToMaxLength(getCellValueAsString(row.getCell(9)), 255)); // Spouse Name
//            customer.setNomineeName(trimToMaxLength(getCellValueAsString(row.getCell(10)), 255)); // Nominee Name
//            customer.setNomineeDob(trimToMaxLength(getCellValueAsString(row.getCell(11)), 255)); // Nominee DOB
//            customer.setNomineeGender(trimToMaxLength(getCellValueAsString(row.getCell(12)), 255)); // Nominee Gender
//            customer.setReferenceName(trimToMaxLength(getCellValueAsString(row.getCell(13)), 255)); // Reference Name
//            customer.setReferenceMobileNumber(trimToMaxLength(getCellValueAsString(row.getCell(14)), 255)); // Reference Number
//            customer.setReferenceName2(trimToMaxLength(getCellValueAsString(row.getCell(15)), 255)); // Reference Name 2
//            customer.setReferenceMobileNumber2(trimToMaxLength(getCellValueAsString(row.getCell(16)), 255)); // Reference Number 2
//            customer.setBranch(trimToMaxLength(getCellValueAsString(row.getCell(17)), 255)); // Branch
            customer.setPanNumber(trimToMaxLength(getCellValueAsString(row.getCell(18)), 255)); // Pan Number
            customer.setAadharNumber(trimToMaxLength(getCellValueAsString(row.getCell(19)), 255)); // Aadhar Number
//            customer.setResidentialType(trimToMaxLength(getCellValueAsString(row.getCell(20)), 255)); // Residential Type
//            customer.setArea(trimToMaxLength(getCellValueAsString(row.getCell(21)), 255)); // Area
            customer.setPinCode(trimToMaxLength(getCellValueAsString(row.getCell(22)), 255)); // Pincode
            customer.setCity(trimToMaxLength(getCellValueAsString(row.getCell(23)), 255)); // City
            customer.setState(trimToMaxLength(getCellValueAsString(row.getCell(24)), 255)); // State
            customer.setAddress(trimToMaxLength(getCellValueAsString(row.getCell(25)), 255)); // Address
//            customer.setLongitude(trimToMaxLength(getCellValueAsString(row.getCell(26)), 255)); // Longitude
//            customer.setLatitude(trimToMaxLength(getCellValueAsString(row.getCell(27)), 255)); // Latitude
            customer.setCompanyName(trimToMaxLength(getCellValueAsString(row.getCell(28)), 255)); // Company Name
            customer.setEmpId(trimToMaxLength(getCellValueAsString(row.getCell(29)), 255)); // Position
            customer.setCompanyAddress(trimToMaxLength(getCellValueAsString(row.getCell(30)), 255)); // Company Address
//            customer.setProfessionalEmail(trimToMaxLength(getCellValueAsString(row.getCell(31)), 255)); // Professional Email
//            customer.setCompanyType(trimToMaxLength(getCellValueAsString(row.getCell(32)), 255)); // Company Type
            customer.setSalary(trimToMaxLength(getCellValueAsString(row.getCell(33)), 255)); // Salary/Income
            customer.setAccountName(trimToMaxLength(getCellValueAsString(row.getCell(34)), 255)); // Account Name
            customer.setAccountNumber(trimToMaxLength(getCellValueAsString(row.getCell(35)), 255)); // Account Number
            customer.setIfscCode(trimToMaxLength(getCellValueAsString(row.getCell(36)), 255)); // IFSC Code
            customer.setBankName(trimToMaxLength(getCellValueAsString(row.getCell(37)), 255)); // Bank Name
//            customer.setBranchName(trimToMaxLength(getCellValueAsString(row.getCell(38)), 255)); // Branch Name
            customer.setBureauDetails(trimToMaxLength(getCellValueAsString(row.getCell(39)), 255));
            customer.setBureauType(trimToMaxLength(getCellValueAsString(row.getCell(40)), 255)); // Bureau Type
            customer.setBureauScore(trimToMaxLength(getCellValueAsString(row.getCell(41)), 255)); // Bureau Score
            customer.setNewToCredit(trimToMaxLength(getCellValueAsString(row.getCell(42)), 255)); // New To Credit
            customer.setOtherDetails(trimToMaxLength(getCellValueAsString(row.getCell(43)), 255)); // Other Details
            customer.setOtherDetails2(trimToMaxLength(getCellValueAsString(row.getCell(44)), 255)); // Other Details 2
//            customer.setInsuranceCompany(trimToMaxLength(getCellValueAsString(row.getCell(45)), 255)); // Insurance Company
//            customer.setCoBorrowerName(trimToMaxLength(getCellValueAsString(row.getCell(46)), 255)); // Co-Borrower Name
//            customer.setCoMobileNumber(trimToMaxLength(getCellValueAsString(row.getCell(47)), 255)); // Co-Mobile Number
//            customer.setCoEmail(trimToMaxLength(getCellValueAsString(row.getCell(48)), 255)); // Co-Email
//            customer.setCoDob(trimToMaxLength(getCellValueAsString(row.getCell(49)), 255)); // Co-DOB
//            customer.setCoGender(trimToMaxLength(getCellValueAsString(row.getCell(50)), 255)); // Co-Gender
//            customer.setCoRelation(trimToMaxLength(getCellValueAsString(row.getCell(51)), 255)); // Co-Relation
//            customer.setCoEmploymentType(trimToMaxLength(getCellValueAsString(row.getCell(52)), 255)); // Co-Employment Type
//            customer.setCoCompanyName(trimToMaxLength(getCellValueAsString(row.getCell(53)), 255)); // Co-Company Name
//            customer.setCoPanNumber(trimToMaxLength(getCellValueAsString(row.getCell(54)), 255)); // Co-Pan Number
//            customer.setCoAadharNumber(trimToMaxLength(getCellValueAsString(row.getCell(55)), 255)); // Co-Aadhar Number
//            customer.setCoNameAsPerBank(trimToMaxLength(getCellValueAsString(row.getCell(56)), 255)); // Co-Name As Per Bank
//            customer.setCoAccountNumber(trimToMaxLength(getCellValueAsString(row.getCell(57)), 255)); // Co-Account Number
//            customer.setCoIfscCode(trimToMaxLength(getCellValueAsString(row.getCell(58)), 255)); // Co-IFSC Code
//            customer.setCoBankName(trimToMaxLength(getCellValueAsString(row.getCell(59)), 255)); // Co-Bank Name
            customer.setCreatedDate(LocalDate.now());
            customer.setStatus(trimToMaxLength(getCellValueAsString(row.getCell(60)),255));
//            customer.setFintechId(trimToMaxLength(getCellValueAsString(row.getCell(61)),255));
            customers.add(customer);
        }

        workbook.close();
        return customers;
    }





















    public void addCustomerData(CustomerRequest customerRequest) {


        CustomerEntity customer = new CustomerEntity();
        // customer.setBorrowerId(customerRequest.getBorrowerId());

        customer.setPartnerLoanId(customerRequest.getPartnerLoanId());
        customer.setBorrowerName(customerRequest.getBorrowerName());
        customer.setFintechName(customerRequest.getFintechName());
//        customer.setFintechId(customerRequest.getFintechId());
        customer.setMobileNumber(customerRequest.getMobileNumber());
        customer.setEmail(customerRequest.getEmail());
        customer.setDob(customerRequest.getDob());
        customer.setGender(customerRequest.getGender());
        customer.setDobJoining(customerRequest.getDobJoining());
        customer.setFatherName(customerRequest.getFatherName());
//        customer.setMaritalStatus(customerRequest.getMaritalStatus());
//        customer.setSpouseName(customerRequest.getSpouseName());
//        customer.setNomineeName(customerRequest.getNomineeName());
//        customer.setNomineeDob(customerRequest.getNomineeDob());
//        customer.setNomineeGender(customerRequest.getNomineeGender());
//        customer.setReferenceName(customerRequest.getReferenceName());
//        customer.setReferenceMobileNumber(customerRequest.getReferenceMobileNumber());
//        customer.setReferenceName2(customerRequest.getReferenceName2());
//        customer.setReferenceMobileNumber2(customerRequest.getReferenceMobileNumber2());
//        customer.setBranch(customerRequest.getBranch());
        customer.setPanNumber(customerRequest.getPanNumber());
        customer.setAadharNumber(customerRequest.getAadharNumber());
//        customer.setResidentialType(customerRequest.getResidentialType());
//        customer.setArea(customerRequest.getArea());
        customer.setPinCode(customerRequest.getPinCode());
        customer.setCity(customerRequest.getCity());
        customer.setState(customerRequest.getState());
        customer.setAddress(customerRequest.getAddress());
//        customer.setLongitude(customerRequest.getLongitude());
//        customer.setLatitude(customerRequest.getLatitude());
        customer.setCompanyName(customerRequest.getCompanyName());
        customer.setCompanyAddress(customerRequest.getCompanyAddress());
//        customer.setProfessionalEmail(customerRequest.getProfessionalEmail());
//        customer.setCompanyType(customerRequest.getCompanyType());
        customer.setSalary(customerRequest.getSalary());
        customer.setEmpId(customerRequest.getEmpId());
        customer.setAccountName(customerRequest.getAccountName());
        customer.setAccountNumber(customerRequest.getAccountNumber());
        customer.setIfscCode(customerRequest.getIfscCode());
        customer.setBankName(customerRequest.getBankName());
//        customer.setBranchName(customerRequest.getBranchName());
        customer.setBureauType(customerRequest.getBureauType());
        customer.setBureauDetails(customerRequest.getBureauDetails());
        customer.setBureauScore(customerRequest.getBureauScore());
        customer.setNewToCredit(customerRequest.getNewToCredit());
        customer.setOtherDetails(customerRequest.getOtherDetails());
        customer.setOtherDetails2(customerRequest.getOtherDetails2());
//        customer.setInsuranceCompany(customerRequest.getInsuranceCompany());
//        customer.setCoBorrowerName(customerRequest.getCoBorrowerName());
//        customer.setCoMobileNumber(customerRequest.getCoMobileNumber());
//        customer.setCoEmail(customerRequest.getCoEmail());
//        customer.setCoDob(customerRequest.getCoDob());
//        customer.setCoGender(customerRequest.getCoGender());
//        customer.setCoRelation(customerRequest.getCoRelation());
//        customer.setCoEmploymentType(customerRequest.getCoEmploymentType());
//        customer.setCoCompanyName(customerRequest.getCoCompanyName());
//        customer.setCoAadharNumber(customerRequest.getCoAadharNumber());
//        customer.setCoPanNumber(customerRequest.getCoPanNumber());
//        customer.setCoNameAsPerBank(customerRequest.getCoNameAsPerBank());
//        customer.setCoBankName(customerRequest.getCoBankName());
//        customer.setCoAccountNumber(customerRequest.getCoAccountNumber());
//        customer.setCoIfscCode(customerRequest.getCoIfscCode());
        customer.setCreatedDate(LocalDate.now());
//        customer.setSpouseName(customerRequest.getSpouseName());
        customer.setStatus(customerRequest.getStatus());


        customerRepository.save(customer);
    }

    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }


    public void updateCustomerDetails(Long id, CustomerRequest customerRequest) {
        Optional<CustomerEntity> customerOptional = customerRepository.findById(id);

        if (customerOptional.isPresent()) {
            CustomerEntity customer = customerOptional.get();

             customer.setPartnerLoanId(customerRequest.getPartnerLoanId());
            customer.setFintechName(customerRequest.getFintechName());
//            customer.setFintechId(customerRequest.getFintechId());
            customer.setBorrowerName(customerRequest.getBorrowerName());
            customer.setMobileNumber(customerRequest.getMobileNumber());
            customer.setEmail(customerRequest.getEmail());
            customer.setDob(customerRequest.getDob());
            customer.setGender(customerRequest.getGender());
            customer.setDobJoining(customerRequest.getDobJoining());
            customer.setFatherName(customerRequest.getFatherName());
//            customer.setMaritalStatus(customerRequest.getMaritalStatus());
//            customer.setSpouseName(customerRequest.getSpouseName());
//            customer.setNomineeName(customerRequest.getNomineeName());
//            customer.setNomineeDob(customerRequest.getNomineeDob());
//            customer.setNomineeGender(customerRequest.getNomineeGender());
//            customer.setReferenceName(customerRequest.getReferenceName());
//            customer.setReferenceMobileNumber(customerRequest.getReferenceMobileNumber());
//            customer.setReferenceName2(customerRequest.getReferenceName2());
//            customer.setReferenceMobileNumber2(customerRequest.getReferenceMobileNumber2());
//            customer.setBranch(customerRequest.getBranch());
            customer.setPanNumber(customerRequest.getPanNumber());
            customer.setAadharNumber(customerRequest.getAadharNumber());
//            customer.setResidentialType(customerRequest.getResidentialType());
//            customer.setArea(customerRequest.getArea());
            customer.setPinCode(customerRequest.getPinCode());
            customer.setCity(customerRequest.getCity());
            customer.setState(customerRequest.getState());
            customer.setAddress(customerRequest.getAddress());
//            customer.setLongitude(customerRequest.getLongitude());
//            customer.setLatitude(customerRequest.getLatitude());
            customer.setCompanyName(customerRequest.getCompanyName());
            customer.setCompanyAddress(customerRequest.getCompanyAddress());
//            customer.setProfessionalEmail(customerRequest.getProfessionalEmail());
//            customer.setCompanyType(customerRequest.getCompanyType());
            customer.setSalary(customerRequest.getSalary());
            customer.setEmpId(customerRequest.getEmpId());
            customer.setAccountName(customerRequest.getAccountName());
            customer.setAccountNumber(customerRequest.getAccountNumber());
            customer.setIfscCode(customerRequest.getIfscCode());
            customer.setBankName(customerRequest.getBankName());
//            customer.setBranchName(customerRequest.getBranchName());
            customer.setBureauDetails(customerRequest.getBureauDetails());
            customer.setBureauType(customerRequest.getBureauType());
            customer.setBureauScore(customerRequest.getBureauScore());
            customer.setNewToCredit(customerRequest.getNewToCredit());
            customer.setOtherDetails(customerRequest.getOtherDetails());
            customer.setOtherDetails2(customerRequest.getOtherDetails2());
//            customer.setInsuranceCompany(customerRequest.getInsuranceCompany());
//            customer.setCoBorrowerName(customerRequest.getCoBorrowerName());
//            customer.setCoMobileNumber(customerRequest.getCoMobileNumber());
//            customer.setCoEmail(customerRequest.getCoEmail());
//            customer.setCoDob(customerRequest.getCoDob());
//            customer.setCoGender(customerRequest.getCoGender());
//            customer.setCoRelation(customerRequest.getCoRelation());
//            customer.setCoEmploymentType(customerRequest.getCoEmploymentType());
//            customer.setCoCompanyName(customerRequest.getCoCompanyName());
//            customer.setCoPanNumber(customerRequest.getCoPanNumber());
//            customer.setCoAadharNumber(customerRequest.getCoAadharNumber());
//            customer.setCoBankName(customerRequest.getCoBankName());
//            customer.setCoNameAsPerBank(customerRequest.getCoNameAsPerBank());
//            customer.setCoAccountNumber(customerRequest.getCoAccountNumber());
//            customer.setCoIfscCode(customerRequest.getCoIfscCode());
            customer.setStatus(customerRequest.getStatus());

             customerRepository.save(customer);
        } else {
            throw new NullPointerException("Customer Details not found with id: " + id);
        }
    }

    public List<CustomerEntity> getAllCustomer() {

        return customerRepository.findAll();
    }

    public byte[] exportAllCustomerDetailsToExcel(List<CustomerEntity> customerDetails) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet("Customer Details");
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "ID", "Partner Loan ID", "Fintech Name", "Borrower Name", "Mobile Number", "Email",
                "Date Of Birth","Dob Joining", "Father Name", "Gender", "PAN Number", "Aadhar Number",
                "Pin Code", "City", "State", "Address","Company Name","Salary","Emp Id","Account Name",
                "Account Number", "IFSC Code", "Bank Name", "Bureau Details", "Bureau Type",
                "Bureau Score", "New To Credit", "Other Details", "Other Details 2", "Status", "Created Date"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (CustomerEntity customer : customerDetails) {
            Row dataRow = sheet.createRow(rowNum++);


            dataRow.createCell(0).setCellValue(customer.getId());
            dataRow.createCell(1).setCellValue(customer.getPartnerLoanId()); // updated field name
            dataRow.createCell(2).setCellValue(customer.getFintechName());
//            dataRow.createCell(3).setCellValue(customer.getFintechId());
            dataRow.createCell(4).setCellValue(customer.getBorrowerName());
            dataRow.createCell(5).setCellValue(customer.getMobileNumber());
            dataRow.createCell(6).setCellValue(customer.getEmail());
            dataRow.createCell(7).setCellValue(customer.getDob());
            dataRow.createCell(8).setCellValue(customer.getGender());
            dataRow.createCell(30).setCellValue(customer.getDobJoining());
            dataRow.createCell(30).setCellValue(customer.getFatherName());
//            dataRow.createCell(9).setCellValue(customer.getMaritalStatus());
//            dataRow.createCell(10).setCellValue(customer.getSpouseName());
//            dataRow.createCell(11).setCellValue(customer.getNomineeName());
//            dataRow.createCell(12).setCellValue(customer.getNomineeDob());
//            dataRow.createCell(13).setCellValue(customer.getNomineeGender());
//            dataRow.createCell(14).setCellValue(customer.getReferenceName());
//            dataRow.createCell(15).setCellValue(customer.getReferenceMobileNumber());
//            dataRow.createCell(16).setCellValue(customer.getReferenceName2());
//            dataRow.createCell(17).setCellValue(customer.getReferenceMobileNumber2());
//            dataRow.createCell(18).setCellValue(customer.getBranch());
            dataRow.createCell(19).setCellValue(customer.getPanNumber());
            dataRow.createCell(20).setCellValue(customer.getAadharNumber());
//            dataRow.createCell(21).setCellValue(customer.getResidentialType());
//            dataRow.createCell(22).setCellValue(customer.getArea());
            dataRow.createCell(23).setCellValue(customer.getPinCode());
            dataRow.createCell(24).setCellValue(customer.getCity());
            dataRow.createCell(25).setCellValue(customer.getState());
            dataRow.createCell(26).setCellValue(customer.getAddress());
//            dataRow.createCell(27).setCellValue(customer.getLongitude());
//            dataRow.createCell(28).setCellValue(customer.getLatitude());
            dataRow.createCell(29).setCellValue(customer.getCompanyName());
            dataRow.createCell(31).setCellValue(customer.getCompanyAddress());
//            dataRow.createCell(32).setCellValue(customer.getProfessionalEmail());
//            dataRow.createCell(33).setCellValue(customer.getCompanyType());
            dataRow.createCell(34).setCellValue(customer.getSalary());
            dataRow.createCell(34).setCellValue(customer.getEmpId());
            dataRow.createCell(35).setCellValue(customer.getAccountName());
            dataRow.createCell(36).setCellValue(customer.getAccountNumber());
            dataRow.createCell(37).setCellValue(customer.getIfscCode());
            dataRow.createCell(38).setCellValue(customer.getBankName());
//            dataRow.createCell(39).setCellValue(customer.getBranchName());
            dataRow.createCell(40).setCellValue(customer.getBureauDetails()); // updated field name
            dataRow.createCell(41).setCellValue(customer.getBureauType());
            dataRow.createCell(42).setCellValue(customer.getBureauScore());
            dataRow.createCell(43).setCellValue(customer.getNewToCredit());
            dataRow.createCell(44).setCellValue(customer.getOtherDetails());
            dataRow.createCell(45).setCellValue(customer.getOtherDetails2());
//            dataRow.createCell(46).setCellValue(customer.getInsuranceCompany());
//            dataRow.createCell(47).setCellValue(customer.getCoBorrowerName());
//            dataRow.createCell(48).setCellValue(customer.getCoMobileNumber());
//            dataRow.createCell(49).setCellValue(customer.getCoEmail());
//            dataRow.createCell(50).setCellValue(customer.getCoDob());
//            dataRow.createCell(51).setCellValue(customer.getCoGender());
//            dataRow.createCell(52).setCellValue(customer.getCoRelation());
//            dataRow.createCell(53).setCellValue(customer.getCoEmploymentType());
//            dataRow.createCell(54).setCellValue(customer.getCoCompanyName());
//            dataRow.createCell(55).setCellValue(customer.getCoPanNumber());
//            dataRow.createCell(56).setCellValue(customer.getCoAadharNumber());
//            dataRow.createCell(57).setCellValue(customer.getCoNameAsPerBank());
//            dataRow.createCell(58).setCellValue(customer.getCoBankName());
//            dataRow.createCell(59).setCellValue(customer.getCoAccountNumber());
//            dataRow.createCell(60).setCellValue(customer.getCoIfscCode());
            dataRow.createCell(61).setCellValue(customer.getStatus());
            dataRow.createCell(62).setCellValue(customer.getCreatedDate().toString());

        }

        workbook.write(byteArrayOutputStream);
        workbook.close();
        return byteArrayOutputStream.toByteArray();
    }

//
//    public List<CustomerEntity> searchByBorrowerId(String borrowerId) {
//        return customerRepository.findByBorrowerId(borrowerId);
//    }

    public List<CustomerEntity> searchByBorrowerName(String borrowerName) {
        return customerRepository.findByBorrowerName(borrowerName);
    }

    public List<CustomerEntity> searchByMobileNumber(String mobileNumber) {
        return customerRepository.findByMobileNumber(mobileNumber);
    }


    public CustomerRequestModel getCustomerDetailsById(Long customerId) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("customer not found with ID: " + customerId));

        CustomerRequestModel customerRequest = new CustomerRequestModel();

        // customerRequest.setBorrowerId(customer.getBorrowerId());
      //  customerRequest.setId(customer.getId());
        customerRequest.setPartnerLoanId(customer.getPartnerLoanId());
        customerRequest.setBorrowerName(customer.getBorrowerName());
        customerRequest.setFintechName(customer.getFintechName());
//        customerRequest.setFintechId(customer.getFintechId());
//        customerRequest.setFintechId(customer.getFintechId());
        customerRequest.setMobileNumber(customer.getMobileNumber());
        customerRequest.setEmail(customer.getEmail());
        customerRequest.setDob(customer.getDob());
        customerRequest.setGender(customer.getGender());
        customerRequest.setDobJoining(customer.getDobJoining());
        customerRequest.setFatherName(customer.getFatherName());
//        customerRequest.setMaritalStatus(customer.getMaritalStatus());
//        customerRequest.setSpouseName(customer.getSpouseName());
//        customerRequest.setNomineeName(customer.getNomineeName());
//        customerRequest.setNomineeDob(customer.getNomineeDob());
//        customerRequest.setNomineeGender(customer.getNomineeGender());
//        customerRequest.setReferenceName(customer.getReferenceName());
//        customerRequest.setReferenceMobileNumber(customer.getReferenceMobileNumber());
//        customerRequest.setReferenceName2(customer.getReferenceName2());
//        customerRequest.setReferenceMobileNumber2(customer.getReferenceMobileNumber2());
//        customerRequest.setBranch(customer.getBranch());
        customerRequest.setPanNumber(customer.getPanNumber());
        customerRequest.setAadharNumber(customer.getAadharNumber());
//        customerRequest.setResidentialType(customer.getResidentialType());
//        customerRequest.setArea(customer.area());
        customerRequest.setPinCode(customer.getPinCode());
        customerRequest.setCity(customer.getCity());
        customerRequest.setState(customer.getState());
        customerRequest.setAddress(customer.getAddress());
//        customerRequest.setLongitude(customer.getLongitude());
//        customerRequest.setLatitude(customer.getLatitude());
        customerRequest.setCompanyName(customer.getCompanyName());
        customerRequest.setCompanyAddress(customer.getCompanyAddress());
//        customerRequest.setProfessionalEmail(customer.getProfessionalEmail());
//        customerRequest.setCompanyType(customer.getCompanyType());
        customerRequest.setSalary(customer.getSalary());
        customerRequest.setEmpId(customer.getEmpId());
        customerRequest.setAccountName(customer.getAccountName());
        customerRequest.setAccountNumber(customer.getAccountNumber());
        customerRequest.setIfscCode(customer.getIfscCode());
        customerRequest.setBankName(customer.getBankName());
//        customerRequest.setBranchName(customer.getBranchName());
        customerRequest.setBureauType(customer.getBureauType());
        customerRequest.setBureauDetails(customer.getBureauDetails());
        customerRequest.setBureauScore(customer.getBureauScore());
        customerRequest.setNewToCredit(customer.getNewToCredit());
        customerRequest.setOtherDetails(customer.getOtherDetails());
        customerRequest.setOtherDetails2(customer.getOtherDetails2());
//        customerRequest.setInsuranceCompany(customer.getInsuranceCompany());
//        customerRequest.setCoBorrowerName(customer.getCoBorrowerName());
//        customerRequest.setCoMobileNumber(customer.getCoMobileNumber());
//        customerRequest.setCoEmail(customer.getCoEmail());
//        customerRequest.setCoDob(customer.getCoDob());
//        customerRequest.setCoGender(customer.getCoGender());
//        customerRequest.setCoRelation(customer.getCoRelation());
//        customerRequest.setCoEmploymentType(customer.getCoEmploymentType());
//        customerRequest.setCoCompanyName(customer.getCoCompanyName());
//        customerRequest.setCoAadharNumber(customer.getCoAadharNumber());
//        customerRequest.setCoPanNumber(customer.getCoPanNumber());
//        customerRequest.setCoNameAsPerBank(customer.getCoNameAsPerBank());
//        customerRequest.setCoBankName(customer.getCoBankName());
//        customerRequest.setCoAccountNumber(customer.getCoAccountNumber());
//        customerRequest.setCoIfscCode(customer.getCoIfscCode());
        customerRequest.setStatus(customer.getStatus());


       // customerRequest.get
        return customerRequest;

    }



//    public List<CustomerEntity> getCustomersByUserFintechId(String userFintechId) {
//        UserEntity user = userRepository.findByFintechId(userFintechId);
//        if (user != null) {
//            return customerRepository.findByFintechId(userFintechId);
//        }
//        return Collections.emptyList();
//    }


    public String completeKyc(Long customerId) {
        Optional<CustomerEntity> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            CustomerEntity customer = customerOpt.get();
            if ("credit check".equalsIgnoreCase(customer.getStatus())) {
                customer.setStatus("Kyc complete");
                customerRepository.save(customer);
                return "KYC completed successfully.";
            } else {
                return "Customer is not in credit check status.";
            }
        } else {
            return "Customer not found.";
        }
    }





    public List<CustomerEntity> getNewCustomersByStatus(String status) {
        return customerRepository.findByStatus(status);
    }
//    public List<CustomerEntity> getByFintechId(String fintechId) {
//        return customerRepository.findByFintechId(fintechId);
//    }


    public long countTotalBorrower() {
        return customerRepository.count();
    }

//    public Resource getSampleCustomerExcel() {
//    String filePath = "C:\\Users\\itdha\\Downloads\\flik\\flik\\src\\main\\resources\\excel\\CustomerDatasample.xlsx";
//    File file = Paths.get(filePath).toFile();
//    if (!file.exists()) {
//        throw new RuntimeException("File not found: " + file.getAbsolutePath());
//    }
//    if (!file.canRead()) {
//        throw new RuntimeException("File is not readable: " + file.getAbsolutePath());
//    }
//    return new FileSystemResource(file);
//}
     public Resource getSampleCustomerExcel() {
    try {
        // Load the file from the resources folder using ClassPathResource
        Resource resource = new ClassPathResource("excel/CustomerDatasample.xlsx");

        // Check if the file exists and is readable
        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("File not found or is not readable: " + resource.getURI());
        }

        return resource;
    } catch (IOException e) {
        throw new RuntimeException("Error accessing file: " + e.getMessage(), e);
    }
}




}









