package com.flik.Service;

import com.flik.entity.CustomerEntity;
import com.flik.entity.LoanEntity;
import com.flik.entity.OverDueEntity;
import com.flik.entity.RepaymentEntity;
import com.flik.repository.LoanRepository;
import com.flik.repository.OverDueRepository;
import com.flik.repository.RepaymentRepository;
import com.flik.request.RepaymentRequest;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class RepaymentService {

    @Autowired
    private RepaymentRepository repaymentRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private OverDueRepository overDueRepository;


    public boolean repaymentsExist(Long loanId)
    {

        return repaymentRepository.existsByLoanId(loanId);

    }

    public List<RepaymentEntity> createReducingRepayments(Long loanId  ) {

        LoanEntity loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));


      //  List<RepaymentEntity> flatRepayments = calculateFlatRepayments(loan);
        List<RepaymentEntity> reducingRepayments = calculateReducingRepayments(loan);

//        List<RepaymentEntity> allRepayments = new ArrayList<>();
//        allRepayments.addAll(flatRepayments);
//        allRepayments.addAll(reducingRepayments);



        return repaymentRepository.saveAll(reducingRepayments);

    }
    public List<RepaymentEntity> createFlatRepayments(Long loanId  ) {

        LoanEntity loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));


        List<RepaymentEntity> flatRepayments = calculateFlatRepayments(loan);
       // List<RepaymentEntity> reducingRepayments = calculateReducingRepayments(loan);

//        List<RepaymentEntity> allRepayments = new ArrayList<>();
//        allRepayments.addAll(flatRepayments);
//        allRepayments.addAll(reducingRepayments);



        return repaymentRepository.saveAll(flatRepayments);

    }



    public List<RepaymentEntity> calculateReducingRepayments(LoanEntity loan) {
        double loanAmount = loan.getLoanAmount();
        double annualRoi = loan.getRoi();
        int tenureInDays = loan.getTenure();
        int roiTypeDays = loan.getRoiType();

        // Calculate the number of EMIs based on tenureInDays and roiTypeDays
        int numberOfEMI = tenureInDays / roiTypeDays;

        // Calculate monthly interest rate
        double monthlyRate = annualRoi / 12 / 100;

        // Calculate EMI using the formula for reducing balance
        double emi = loanAmount * (monthlyRate * Math.pow(1 + monthlyRate, numberOfEMI)) /
                (Math.pow(1 + monthlyRate, numberOfEMI) - 1);

        // Initialize variables
        LocalDate disbursedDate = LocalDate.now(); // Assuming disbursed date is today; adjust as needed
        LocalDate dueDate = disbursedDate.plusDays(roiTypeDays);
        double outstandingPrincipal = loanAmount;

        List<RepaymentEntity> repayments = new ArrayList<>();

        for (int i = 0; i < numberOfEMI; i++) {
            // Calculate interest and principal components
            double interestComponent = outstandingPrincipal * monthlyRate;
            double principalComponent = emi - interestComponent;
            outstandingPrincipal -= principalComponent;

            double roundedInterestComponent = Math.round(interestComponent * 100.0) / 100.0;
            double roundedPrincipalComponent = Math.round(principalComponent * 100.0) / 100.0;
            double roundedEmi = Math.round(emi * 100.0) / 100.0;
            double totalPaid = emi * (i + 1);

            // Create repayment entity
            RepaymentEntity repayment = new RepaymentEntity();
            repayment.setLoanId(loan.getId());
            repayment.setCustomerId(loan.getCustomerId());
            repayment.setPartnerLoanId(loan.getPartnerLoanId());
            repayment.setPartnerName(loan.getPartnerName());
            repayment.setBranchName(loan.getBranchName());
            repayment.setBorrowerName(loan.getBorrowerName());
            repayment.setMobileNumber(loan.getMobileNumber());
            repayment.setRepaymentAmount(String.format("%.2f", roundedEmi));
            repayment.setDueDate(dueDate);
            repayment.setCollectionDate(dueDate.toString());
            repayment.setBounceCharge(loan.getBounceCharge());
            repayment.setPenalCharge(loan.getPenalCharge());
            repayment.setTotalPaid(String.format("%.2f", totalPaid));
            repayment.setDisbursedDate(disbursedDate);
            repayment.setStatus("Pending");
            repayment.setRepaymentType("Reducing");

            repayment.setInterestPart(roundedInterestComponent);
            repayment.setPrincipalPart(roundedPrincipalComponent);
            repayment.setOutstandingPrincipal(Math.round(outstandingPrincipal * 100.0) / 100.0);

            repayments.add(repayment);

            // Update due date for next EMI
            dueDate = dueDate.plusDays(roiTypeDays);
        }

        return repayments;
    }

    public List<RepaymentEntity> calculateFlatRepayments(LoanEntity loan) {
        double loanAmount = loan.getLoanAmount();
        double annualRoi = loan.getRoi();
        int tenureInDays = loan.getTenure();
        int roiTypeDays = loan.getRoiType(); // e.g., 30 for monthly

        // Calculate the number of EMIs based on tenureInDays and roiTypeDays
        int numberOfEMI = tenureInDays / roiTypeDays;

        // Convert annual ROI to decimal and tenure to years
        double annualRate = annualRoi / 100;
        double tenureInYears = (double) tenureInDays / 365;

        // Calculate total interest
        double totalInterest = loanAmount * annualRate * tenureInYears;

        // Calculate EMI using flat rate formula
        double emi = (loanAmount + totalInterest) / numberOfEMI;

        // Initialize variables
        LocalDate disbursedDate = LocalDate.now(); // Assuming disbursed date is today; adjust as needed
        LocalDate dueDate = disbursedDate.plusDays(roiTypeDays);

        List<RepaymentEntity> repayments = new ArrayList<>();

        for (int i = 0; i < numberOfEMI; i++) {
            // Interest component is the total interest divided by the number of EMIs
            double interestComponent = totalInterest / numberOfEMI;
            // Principal component is the EMI minus the interest component
            double principalComponent = emi - interestComponent;
            // The outstanding principal decreases over time
            double outstandingPrincipal = loanAmount - (principalComponent * (i + 1));
            double totalPaid = emi * (i + 1);

            // Round values
            double roundedInterestComponent = Math.round(interestComponent * 100.0) / 100.0;
            double roundedPrincipalComponent = Math.round(principalComponent * 100.0) / 100.0;
            double roundedEmi = Math.round(emi * 100.0) / 100.0;

            // Create repayment entity
            RepaymentEntity repayment = new RepaymentEntity();
            repayment.setLoanId(loan.getId());
            repayment.setCustomerId(loan.getCustomerId());
            repayment.setPartnerLoanId(loan.getPartnerLoanId());
            repayment.setPartnerName(loan.getPartnerName());
            repayment.setBranchName(loan.getBranchName());
            repayment.setBorrowerName(loan.getBorrowerName());
            repayment.setMobileNumber(loan.getMobileNumber());
            repayment.setRepaymentAmount(String.format("%.2f", roundedEmi));
            repayment.setDueDate(dueDate);
            repayment.setCollectionDate(dueDate.toString());
            repayment.setBounceCharge(loan.getBounceCharge());
            repayment.setPenalCharge(loan.getPenalCharge());
            repayment.setTotalPaid(String.format("%.2f", totalPaid));
            repayment.setDisbursedDate(disbursedDate);
            repayment.setStatus("Pending");
            repayment.setRepaymentType("Flat");

            repayment.setInterestPart(roundedInterestComponent);
            repayment.setPrincipalPart(roundedPrincipalComponent);
            repayment.setOutstandingPrincipal(Math.round(outstandingPrincipal * 100.0) / 100.0);

            repayments.add(repayment);

            // Update due date for next EMI
            dueDate = dueDate.plusDays(roiTypeDays);
        }

        return repayments;
    }



    public List<RepaymentEntity> getRepaymentsByLoanId(Long loanId)
    {
        return repaymentRepository.findByLoanId( loanId);
    }
    public List<RepaymentEntity> getRepaymentsByCustomerId(String customerId) {
        return repaymentRepository.findByCustomerId(customerId);
    }


    public void save(MultipartFile file) {
        try {
            List<RepaymentEntity> repaymentEntities = saveExcelFile(file.getInputStream());
//            if (customers.size() < 1 || customers.size() > 10) {
//                throw new IllegalArgumentException("Excel file must contain between 1 and 10 rows.");
//            }
            repaymentRepository.saveAll(repaymentEntities);
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

    private List<RepaymentEntity> saveExcelFile(InputStream inputStream) throws IOException {
        List<RepaymentEntity> repaymentEntities = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            RepaymentEntity repayment = new RepaymentEntity();

            repayment.setLoanId(Long.valueOf(trimToMaxLength(getCellValueAsString(row.getCell(1)), 255)));
            repayment.setCustomerId(trimToMaxLength(getCellValueAsString(row.getCell(2)), 255));



           repayment.setDisbursedDate(convertToLocalDate(row.getCell(3))); // Assuming date format

            repayment.setPartnerLoanId(trimToMaxLength(getCellValueAsString(row.getCell(4)), 255));
            repayment.setPartnerName(trimToMaxLength(getCellValueAsString(row.getCell(5)), 255));
            repayment.setBranchName(trimToMaxLength(getCellValueAsString(row.getCell(6)), 255));
            repayment.setBorrowerName(trimToMaxLength(getCellValueAsString(row.getCell(7)), 255));
            repayment.setMobileNumber(trimToMaxLength(getCellValueAsString(row.getCell(8)), 15));
            repayment.setRepaymentAmount(trimToMaxLength(getCellValueAsString(row.getCell(9)), 255));

            repayment.setDueDate(convertToLocalDate(row.getCell(10)));

            repayment.setCollectionDate(trimToMaxLength(getCellValueAsString(row.getCell(11)), 255));
            repayment.setBounceCharge(trimToMaxLength(getCellValueAsString(row.getCell(12)), 255));
            repayment.setPenalCharge(trimToMaxLength(getCellValueAsString(row.getCell(13)), 255));
            repayment.setTotalPaid(trimToMaxLength(getCellValueAsString(row.getCell(14)), 255));
            repayment.setStatus(trimToMaxLength(getCellValueAsString(row.getCell(15)), 50)); // paid & pending
            repayment.setRoiType(trimToMaxLength(getCellValueAsString(row.getCell(16)), 50));
            repayment.setRepaymentType(trimToMaxLength(getCellValueAsString(row.getCell(17)), 50)); // flat & reducing
            repayment.setTransactionNumber(trimToMaxLength(getCellValueAsString(row.getCell(18)), 255));
            repayment.setModeOfPayment(trimToMaxLength(getCellValueAsString(row.getCell(19)), 255));
            repayment.setInterestPart(Double.valueOf((getCellValueAsString(row.getCell(20)))));
            repayment.setPrincipalPart(Double.valueOf((getCellValueAsString(row.getCell(21)))));
            repayment.setOutstandingPrincipal(Double.valueOf(getCellValueAsString(row.getCell(22))));


            repaymentEntities.add(repayment);
        }


        workbook.close();
        repaymentRepository.saveAll(repaymentEntities);
        return repaymentEntities;
    }
    private LocalDate convertToLocalDate(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Handle date formatted cells directly from Excel
                    return cell.getLocalDateTimeCellValue().toLocalDate();
                } else {
                    // Handle numeric values as Excel serial dates
                    return DateUtil.getJavaDate(cell.getNumericCellValue()).toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                }
            case STRING:
                String dateString = cell.getStringCellValue().trim();
                System.out.println("Parsing date from string: " + dateString);

                // Parse the date string in "dd-MM-yyyy" format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                try {
                    return LocalDate.parse(dateString, formatter);
                } catch (DateTimeParseException e) {
                    System.err.println("Failed to parse date in dd-MM-yyyy format: " + dateString);
                    return null;
                }
            default:
                System.err.println("Unsupported cell type for date conversion: " + cell.getCellType());
                return null;
        }
    }


    private String trimToMaxLength(String value, int maxLength) {
        if (value != null && value.length() > maxLength) {
            return value.substring(0, maxLength);
        }
        return value;
    }



    public List<RepaymentEntity> getDueEmisForToday() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");  // Adjust pattern to match your date format
        String todayString = today.format(formatter);
        return repaymentRepository.findByDueDate(LocalDate.parse(todayString));
    }




    @Transactional
    public void updateRepaymentStatus(Long repaymentId, String status) {
        RepaymentEntity repaymentEntity = repaymentRepository.findById(repaymentId)
                .orElseThrow(() -> new RuntimeException("Repayment not found"));

        repaymentEntity.setStatus(status);
        repaymentRepository.save(repaymentEntity);

        if (status.equalsIgnoreCase("Paid")) {
            // Delete from Overdue table if exists
            OverDueEntity overDueEntity = overDueRepository.findByRepaymentId(repaymentId).orElse(null);
            if (overDueEntity != null) {
                overDueRepository.delete(overDueEntity);
            }
        } else if (status.equalsIgnoreCase("Pending")) {
            // Ensure the repayment is in the overdue table if it is pending
            OverDueEntity overDueEntity = overDueRepository.findByRepaymentId(repaymentId).orElse(null);
            if (overDueEntity == null) {
                overDueEntity = new OverDueEntity();
                overDueEntity.setLoanId(repaymentEntity.getLoanId());
                overDueEntity.setRepaymentId(repaymentEntity.getId());
                overDueEntity.setPartnerLoanId(repaymentEntity.getPartnerLoanId());
                overDueEntity.setBorrowerName(repaymentEntity.getBorrowerName());
                overDueEntity.setMobileNumber(repaymentEntity.getMobileNumber());
                overDueEntity.setDueDate(repaymentEntity.getDueDate());
                overDueEntity.setBounceCharge(repaymentEntity.getBounceCharge());
                overDueEntity.setPenalCharge(repaymentEntity.getPenalCharge());
//                overDueEntity.setDueEmiCount(repaymentEntity.getRepaymentAmount());
                overDueEntity.setDisbursedDate(repaymentEntity.getDisbursedDate());
                overDueEntity.setOverdueAmount(repaymentEntity.getRepaymentAmount());
                overDueEntity.setStatus("Pending");

                Double repaymentAmount = Double.parseDouble(repaymentEntity.getRepaymentAmount());
                Double penalCharge = Double.parseDouble(repaymentEntity.getPenalCharge());
                Double totalPendingAmount = repaymentAmount + penalCharge;
                overDueEntity.setTotalPendingAmount(totalPendingAmount);

                overDueRepository.save(overDueEntity);
            }
        }
    }

//    public void checkAndHandleOverduePayments() {
//        try {
//            List<RepaymentEntity> pendingRepayments = repaymentRepository.findByStatus("Pending");
//
//            for (RepaymentEntity repayment : pendingRepayments) {
//                LocalDate now = LocalDate.now();
//                LocalDate dueDate = LocalDate.parse(repayment.getDueDate());
//
//                if (now.isAfter(dueDate)) {
//                    OverDueEntity overdue = new OverDueEntity();
//                    overdue.setLoanId( repayment.getLoanId());
//                    overdue.setRepaymentId(repayment.getId());
//                    overdue.setPartnerLoanId(repayment.getPartnerLoanId());
//                    overdue.setBorrowerName(repayment.getBorrowerName());
//                    overdue.setMobileNumber(repayment.getMobileNumber());
//                    overdue.setDueDate(repayment.getDueDate());
//                    overdue.setBounceCharge(repayment.getBounceCharge());
//                    overdue.setPenalCharge(repayment.getPenalCharge());
//                    overdue.setDueEmiCount(repayment.getRepaymentAmount());
//                    overdue.setStatus("Pending");
//
//                    Double repaymentAmount = Double.parseDouble(repayment.getRepaymentAmount());
//                    Double penalCharge = Double.parseDouble(repayment.getPenalCharge());
//                    Double totalPendingAmount = repaymentAmount + penalCharge;
//                    overdue.setTotalPendingAmount(totalPendingAmount);
//
//                    overDueRepository.save(overdue);
//                }
//            }
//        } catch (Exception e) {
//            // Handle exceptions or log errors here
//            e.printStackTrace();
//            throw new RuntimeException("Error checking and handling overdue payments: " + e.getMessage(), e);
//        }
//
//    }

//    public int countPaidEmis(Long loanId) {
//        // Retrieve all repayments with status "Paid"
//        List<RepaymentEntity> paidRepayments = repaymentRepository.findByStatus("Paid");
//
//        // Retrieve all repayments for the given loanId
//        List<RepaymentEntity> repaymentsByLoanId = repaymentRepository.findByLoanId(String.valueOf(loanId));
//
//        // Calculate the intersection of paid repayments and repayments for the given loanId
//        int count = 0;
//        for (RepaymentEntity repayment : repaymentsByLoanId) {
//            if (paidRepayments.contains(repayment)) {
//                count++;
//            }
//        }
//
//        return count;
//    }






    public List<RepaymentEntity> searchByBorrowerName(String borrowerName) {
        return repaymentRepository.findByBorrowerName(borrowerName);
    }

    public List<RepaymentEntity> searchByMobileNumber(Long mobileNumber) {
        return repaymentRepository.findByLoanId(mobileNumber);
    }

//List of RepaymentTable
    public RepaymentRequest getRepaymentById(Long repaymentId) {
        RepaymentEntity repayment = repaymentRepository.findById(repaymentId)
                .orElseThrow(() -> new RuntimeException("loanId not found with ID: " + repaymentId));

        RepaymentRequest repaymentRequest = new RepaymentRequest();

        repaymentRequest.setLoanId(repayment.getLoanId());
        repaymentRequest.setPartnerLoanId(repayment.getPartnerLoanId());
        repaymentRequest.setPartnerName(repayment.getPartnerName());
        repaymentRequest.setBranchName(repayment.getBranchName());
        repaymentRequest.setBorrowerName(repayment.getBorrowerName());
        repaymentRequest.setMobileNumber(repayment.getMobileNumber());
        repaymentRequest.setRepayment(repayment.getRepaymentAmount());
        repaymentRequest.setDueDate(repayment.getDueDate());
        repaymentRequest.setCollectionDate(repayment.getCollectionDate());
        repaymentRequest.setBounceCharge(repayment.getBounceCharge());
        repaymentRequest.setPenalCharge(repayment.getPenalCharge());
        repaymentRequest.setTotalPaid(repayment.getTotalPaid());
        repaymentRequest.setStatus(repayment.getStatus());
        repaymentRequest.setTransactionNumber(repayment.getTransactionNumber());
        repaymentRequest.setModeOfPayment(repayment.getModeOfPayment());


        return repaymentRequest;

    }



    public List<RepaymentEntity> getAllRepaymentList() {

        return repaymentRepository.findAll();

    }

    public byte[] exportAllRepaymentDetailsToExcel(List<RepaymentEntity> repaymentDetails) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet("Repayment Details");
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "ID", "Loan ID", "Disbursed Date", "Partner Loan ID", "Partner Name",
                "Branch Name", "Borrower Name", "Mobile Number", "Repayment Amount",
                "Due Date", "Collection Date", "Bounce Charge", "Penal Charge",
                "Total Paid", "Status", "Transaction Number", "Mode Of Payment"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (RepaymentEntity entity : repaymentDetails) {
            Row dataRow = sheet.createRow(rowNum++);

            dataRow.createCell(0).setCellValue(entity.getId());
            dataRow.createCell(1).setCellValue(entity.getLoanId());
            dataRow.createCell(2).setCellValue(entity.getDisbursedDate() != null ? entity.getDisbursedDate().toString() : "");
            dataRow.createCell(3).setCellValue(entity.getPartnerLoanId());
            dataRow.createCell(4).setCellValue(entity.getPartnerName());
            dataRow.createCell(5).setCellValue(entity.getBranchName());
            dataRow.createCell(6).setCellValue(entity.getBorrowerName());
            dataRow.createCell(7).setCellValue(entity.getMobileNumber());
            dataRow.createCell(8).setCellValue(entity.getRepaymentAmount());
            dataRow.createCell(9).setCellValue(entity.getDueDate() != null ? entity.getDueDate().toString() : "");
            dataRow.createCell(10).setCellValue(entity.getCollectionDate());
            dataRow.createCell(11).setCellValue(entity.getBounceCharge());
            dataRow.createCell(12).setCellValue(entity.getPenalCharge());
            dataRow.createCell(13).setCellValue(entity.getTotalPaid());
            dataRow.createCell(14).setCellValue(entity.getStatus());
            dataRow.createCell(15).setCellValue(entity.getTransactionNumber());
            dataRow.createCell(16).setCellValue(entity.getModeOfPayment());
        }

        workbook.write(byteArrayOutputStream);
        workbook.close();
        return byteArrayOutputStream.toByteArray();
    }

    public List<OverDueEntity> getAllOverDueList() {

        return overDueRepository.findAll();
    }

    public byte[] exportAllOverdueDetailsToExcel(List<OverDueEntity> overdueDetails) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet("Overdue Details");
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "ID", "Loan ID", "Repayment ID", "Partner Loan ID", "Borrower Name",
                "Mobile Number", "Disbursed Date", "Due Date", "Due EMI Count",
                "Overdue Amount", "Bounce Charge", "Penal Charge", "Total Pending Amount", "Status"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (OverDueEntity overdue : overdueDetails) {
            Row dataRow = sheet.createRow(rowNum++);

            dataRow.createCell(0).setCellValue(overdue.getId());
            dataRow.createCell(1).setCellValue(overdue.getLoanId());
            dataRow.createCell(2).setCellValue(overdue.getRepaymentId());
            dataRow.createCell(3).setCellValue(overdue.getPartnerLoanId());
            dataRow.createCell(4).setCellValue(overdue.getBorrowerName());
            dataRow.createCell(5).setCellValue(overdue.getMobileNumber());
            dataRow.createCell(6).setCellValue(overdue.getDisbursedDate() != null ? overdue.getDisbursedDate().toString() : "");
            dataRow.createCell(7).setCellValue(overdue.getDueDate() != null ? overdue.getDueDate().toString() : "");
            dataRow.createCell(8).setCellValue(overdue.getDueEmiCount());
            dataRow.createCell(9).setCellValue(overdue.getOverdueAmount());
            dataRow.createCell(10).setCellValue(overdue.getBounceCharge());
            dataRow.createCell(11).setCellValue(overdue.getPenalCharge());
            dataRow.createCell(12).setCellValue(overdue.getTotalPendingAmount());
            dataRow.createCell(13).setCellValue(overdue.getStatus());
        }

        workbook.write(byteArrayOutputStream);
        workbook.close();
        return byteArrayOutputStream.toByteArray();
    }


}