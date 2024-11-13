//package com.flik.Service;
//
//import com.flik.entity.CustomerEntity;
//import com.flik.repository.CustomerRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import java.io.IOException;
//import java.io.InputStream;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//@Service
//public class CustomerService {
//
//    private final CustomerRepository customerRepository;
//
//    public CustomerService(CustomerRepository customerRepository) {
//        this.customerRepository = customerRepository;
//    }
//
//    // Method to read the Excel file and return the list of customers for preview
//    public List<CustomerEntity> previewCustomers(MultipartFile file) throws IOException {
//        return readExcelFile(file.getInputStream());
//    }
//
//
//     private List<CustomerEntity> readExcelFile(InputStream inputStream) throws IOException {
//        List<CustomerEntity> customers = new ArrayList<>();
//        Workbook workbook = new XSSFWorkbook(inputStream);
//        Sheet sheet = workbook.getSheetAt(0);
//
//        for (Row row : sheet) {
//            if (row.getRowNum() == 0) {
//                continue;
//            }
//            CustomerEntity customer = new CustomerEntity();
//            // Set customer fields based on the columns
//            customer.setPartnerLoanId(trimToMaxLength(getCellValueAsString(row.getCell(1)), 255)); // Partner Loan ID
//            customer.setFintechName(trimToMaxLength(getCellValueAsString(row.getCell(2)), 255)); // Fintech Partner Name
//            customer.setBorrowerName(trimToMaxLength(getCellValueAsString(row.getCell(3)), 255)); // Borrower Name
//            customer.setMobileNumber(trimToMaxLength(getCellValueAsString(row.getCell(4)), 255)); // Mobile Number
//            customer.setEmail(trimToMaxLength(getCellValueAsString(row.getCell(5)), 255)); // Email
//            customer.setDob(trimToMaxLength(getCellValueAsString(row.getCell(6)), 255)); // DOB
//            customer.setGender(trimToMaxLength(getCellValueAsString(row.getCell(7)), 255)); // Gender
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
//            customer.setPanNumber(trimToMaxLength(getCellValueAsString(row.getCell(18)), 255)); // Pan Number
//            customer.setAadharNumber(trimToMaxLength(getCellValueAsString(row.getCell(19)), 255)); // Aadhar Number
//            customer.setResidentialType(trimToMaxLength(getCellValueAsString(row.getCell(20)), 255)); // Residential Type
//            customer.setArea(trimToMaxLength(getCellValueAsString(row.getCell(21)), 255)); // Area
//            customer.setPinCode(trimToMaxLength(getCellValueAsString(row.getCell(22)), 255)); // Pincode
//            customer.setCity(trimToMaxLength(getCellValueAsString(row.getCell(23)), 255)); // City
//            customer.setState(trimToMaxLength(getCellValueAsString(row.getCell(24)), 255)); // State
//            customer.setAddress(trimToMaxLength(getCellValueAsString(row.getCell(25)), 255)); // Address
//            customer.setLongitude(trimToMaxLength(getCellValueAsString(row.getCell(26)), 255)); // Longitude
//            customer.setLatitude(trimToMaxLength(getCellValueAsString(row.getCell(27)), 255)); // Latitude
//            customer.setCompanyName(trimToMaxLength(getCellValueAsString(row.getCell(28)), 255)); // Company Name
//            customer.setPosition(trimToMaxLength(getCellValueAsString(row.getCell(29)), 255)); // Position
//            customer.setCompanyAddress(trimToMaxLength(getCellValueAsString(row.getCell(30)), 255)); // Company Address
//            customer.setProfessionalEmail(trimToMaxLength(getCellValueAsString(row.getCell(31)), 255)); // Professional Email
//            customer.setCompanyType(trimToMaxLength(getCellValueAsString(row.getCell(32)), 255)); // Company Type
//            customer.setSalary(trimToMaxLength(getCellValueAsString(row.getCell(33)), 255)); // Salary/Income
//            customer.setAccountName(trimToMaxLength(getCellValueAsString(row.getCell(34)), 255)); // Account Name
//            customer.setAccountNumber(trimToMaxLength(getCellValueAsString(row.getCell(35)), 255)); // Account Number
//            customer.setIfscCode(trimToMaxLength(getCellValueAsString(row.getCell(36)), 255)); // IFSC Code
//            customer.setBankName(trimToMaxLength(getCellValueAsString(row.getCell(37)), 255)); // Bank Name
//            customer.setBranchName(trimToMaxLength(getCellValueAsString(row.getCell(38)), 255)); // Branch Name
//            customer.setBureauDetails(trimToMaxLength(getCellValueAsString(row.getCell(39)), 255));
//            customer.setBureauType(trimToMaxLength(getCellValueAsString(row.getCell(40)), 255)); // Bureau Type
//            customer.setBureauScore(trimToMaxLength(getCellValueAsString(row.getCell(41)), 255)); // Bureau Score
//            customer.setNewToCredit(trimToMaxLength(getCellValueAsString(row.getCell(42)), 255)); // New To Credit
//            customer.setOtherDetails(trimToMaxLength(getCellValueAsString(row.getCell(43)), 255)); // Other Details
//            customer.setOtherDetails2(trimToMaxLength(getCellValueAsString(row.getCell(44)), 255)); // Other Details 2
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
//            customer.setCreatedDate(LocalDate.now());
//            customer.setStatus(trimToMaxLength(getCellValueAsString(row.getCell(60)),255));
//            customer.setFintechId(trimToMaxLength(getCellValueAsString(row.getCell(61)),255));
//            customers.add(customer);
//        }
//
//        workbook.close();
//        return customers;
//    }
//
//    // Utility method to convert cell value to string
//    private static String getCellValueAsString(Cell cell) {
//        if (cell == null) {
//            return "";
//        }
//        switch (cell.getCellType()) {
//            case STRING:
//                return cell.getStringCellValue();
//            case NUMERIC:
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    return cell.getDateCellValue().toString();
//                } else {
//                    return String.valueOf(cell.getNumericCellValue());
//                }
//            case BOOLEAN:
//                return String.valueOf(cell.getBooleanCellValue());
//            case FORMULA:
//                return cell.getCellFormula();
//            case BLANK:
//                return "";
//            default:
//                return "";
//        }
//    }
//
//    // Utility method to trim string length
//    private String trimToMaxLength(String value, int maxLength) {
//        return value != null && value.length() > maxLength ? value.substring(0, maxLength) : value;
//    }
//}
