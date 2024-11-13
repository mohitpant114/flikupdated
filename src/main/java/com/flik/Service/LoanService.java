package com.flik.Service;

import com.flik.entity.CustomerVerified;
import com.flik.entity.LoanEntity;
import com.flik.repository.CustomerVerifiedRepository;
import com.flik.repository.LoanRepository;
import com.flik.request.LoanRequest;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;


    @Autowired
    private CustomerVerifiedRepository customerVerifiedRepository;

    public void save(MultipartFile file) {
        try {
            List<LoanEntity> loanEntities = readExcelFile(file.getInputStream());
//            if (loanEntities.size() < 1 || loanEntities.size() > 10) {
//                throw new IllegalArgumentException("Excel file must contain between 1 and 10 rows.");
//            }
            loanRepository.saveAll(loanEntities);
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

//                if (DateUtil.isCellDateFormatted(cell)) {
//                    return cell.getDateCellValue().toString();
//                } else {
//                    return String.valueOf(cell.getNumericCellValue());
//                }
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
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
    private List<LoanEntity> readExcelFile(InputStream inputStream) throws IOException {
        List<LoanEntity> loanEntities = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip header row
            }
            LoanEntity loanEntity = new LoanEntity();

            // approvedDate
            String approvedDateString = getCellValueAsString(row.getCell(1));
            if (!approvedDateString.isEmpty()) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date approvedDate = dateFormat.parse(approvedDateString);
                    loanEntity.setApprovedDate(approvedDate);
                } catch (ParseException e) {
                    System.err.println("Failed to parse approved date: " + approvedDateString);
                    e.printStackTrace();
                }
            }

            // UTR
            String utr = getCellValueAsString(row.getCell(2));
            if (!utr.isEmpty()) {
                loanEntity.setUtr(utr);
            }

            // Set other fields directly

            loanEntity.setUmrnNumber(getCellValueAsString(row.getCell(3)));
            loanEntity.setESign(getCellValueAsString(row.getCell(4)));
            loanEntity.setPartnerLoanId(getCellValueAsString(row.getCell(5)));
            loanEntity.setPartnerName(getCellValueAsString(row.getCell(6)));
            loanEntity.setBranchName(getCellValueAsString(row.getCell(7)));
            loanEntity.setBorrowerName(getCellValueAsString(row.getCell(8)));
            loanEntity.setMobileNumber(getCellValueAsString(row.getCell(9)));
            loanEntity.setEmailId(getCellValueAsString(row.getCell(10)));
            loanEntity.setLoanType(getCellValueAsString(row.getCell(11)));

            // Set numeric fields
            String loanAmountStr = getCellValueAsString(row.getCell(12));
            if (!loanAmountStr.isEmpty()) {
                loanEntity.setLoanAmount(Double.valueOf(loanAmountStr));
            }

            loanEntity.setProcessingFeeCharge(getCellValueAsString(row.getCell(13)));
            loanEntity.setProcessingFee(getCellValueAsString(row.getCell(14)));
            loanEntity.setGst(getCellValueAsString(row.getCell(15)));
            loanEntity.setDisbursedAmount(getCellValueAsString(row.getCell(16)));

            String tenureStr = getCellValueAsString(row.getCell(17));
            if (!tenureStr.isEmpty()) {
                loanEntity.setTenure(Integer.valueOf(tenureStr));
            }

            loanEntity.setNumberOfEmi(getCellValueAsString(row.getCell(18)));
            //loanEntity.setRoiType(getCellValueAsString(row.getCell(19)));
            String roiTypeStr = getCellValueAsString(row.getCell(19));
            if (!roiTypeStr.isEmpty()) {
                loanEntity.setRoiType(Integer.valueOf(roiTypeStr));
            }

            String roiStr = getCellValueAsString(row.getCell(20));
            if (!roiStr.isEmpty()) {
                loanEntity.setRoi(Double.valueOf(roiStr));
            }

            loanEntity.setRepayment(getCellValueAsString(row.getCell(21)));

            // disbursedDate
            String disbursedDateString = getCellValueAsString(row.getCell(22));
            if (!disbursedDateString.isEmpty()) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date disbursedDate = dateFormat.parse(disbursedDateString);
                      loanEntity.setDisbursedDate (disbursedDate);
                } catch (ParseException e) {
                    System.err.println("Failed to parse disbursed date: " + disbursedDateString);
                    e.printStackTrace();
                }
            }
            String collectionDateString = getCellValueAsString(row.getCell(23));
            if (!collectionDateString.isEmpty()) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date collectionDate = dateFormat.parse(collectionDateString);
                    // Uncomment if you add disbursedDate field to LoanEntity
                    loanEntity.setCollectionDate (collectionDate);
                } catch (ParseException e) {
                    System.err.println("Failed to parse disbursed date: " + disbursedDateString);
                    e.printStackTrace();
                }
            }




            // loanEntity.setCollectionDate(getCellValueAsString(row.getCell(23)));
            loanEntity.setBounceCharge(getCellValueAsString(row.getCell(24)));
            loanEntity.setPenalCharge(getCellValueAsString(row.getCell(25)));

            // Set remaining numeric fields
            String interestPartStr = getCellValueAsString(row.getCell(26));
            if (!interestPartStr.isEmpty()) {
                loanEntity.setInterestPart(Double.valueOf(interestPartStr));
            }

            loanEntity.setPrincipalPart(getCellValueAsString(row.getCell(27)));
            loanEntity.setTotalPaid(getCellValueAsString(row.getCell(28)));
            loanEntity.setStatus(getCellValueAsString(row.getCell(29)));

            loanEntity.setCustomerId(getCellValueAsString(row.getCell(30 )));

             loanEntity.setCreatedDate(LocalDateTime.now());

            loanEntities.add(loanEntity);
        }

        workbook.close();
        return loanEntities;
    }

//    private List<LoanEntity> readExcelFile(InputStream inputStream) throws IOException {
//        List<LoanEntity> loanEntities = new ArrayList<>();
//        Workbook workbook = new XSSFWorkbook(inputStream);
//        Sheet sheet = workbook.getSheetAt(0);
//
//        for (Row row : sheet) {
//            if (row.getRowNum() == 0) {
//                continue; // Skip header row
//            }
//            LoanEntity loanEntity = new LoanEntity();
//
//            // approvedDate
//            String approvedDateString = getCellValueAsString(row.getCell(1));
//            System.out.println("Approved Date: " + approvedDateString); // Log cell value
//            if (isValidDate(approvedDateString)) {
//                try {
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                    Date approvedDate = dateFormat.parse(approvedDateString);
//                    loanEntity.setApprovedDate(approvedDate);
//                } catch (ParseException e) {
//                    System.err.println("Failed to parse approved date: " + approvedDateString);
//                    e.printStackTrace();
//
//            }}
//
//            // Set string fields directly
//            loanEntity.setUtr(getCellValueAsString(row.getCell(2)));
//            System.out.println("UTR: " + loanEntity.getUtr()); // Log UTR value
//            loanEntity.setUmrnNumber(getCellValueAsString(row.getCell(3)));
//            loanEntity.setESign(getCellValueAsString(row.getCell(4)));
//            loanEntity.setPartnerLoanId(getCellValueAsString(row.getCell(5)));
//            loanEntity.setPartnerName(getCellValueAsString(row.getCell(6)));
//            loanEntity.setBranchName(getCellValueAsString(row.getCell(7)));
//            loanEntity.setBorrowerName(getCellValueAsString(row.getCell(8)));
//            loanEntity.setMobileNumber(getCellValueAsString(row.getCell(9)));
//            loanEntity.setEmailId(getCellValueAsString(row.getCell(10)));
//            loanEntity.setLoanType(getCellValueAsString(row.getCell(11)));
//
//            // Set numeric fields
//            loanEntity.setLoanAmount(Double.valueOf(getCellValueAsString(row.getCell(12))));
//            loanEntity.setProcessingFeeCharge(getCellValueAsString(row.getCell(13)));
//            loanEntity.setProcessingFee(getCellValueAsString(row.getCell(14)));
//            loanEntity.setGst(getCellValueAsString(row.getCell(15)));
//            loanEntity.setDisbursedAmount(getCellValueAsString(row.getCell(16)));
//            loanEntity.setTenure(Integer.valueOf(getCellValueAsString(row.getCell(17))));
//            loanEntity.setNumberOfEmi(getCellValueAsString(row.getCell(18)));
//            loanEntity.setRoiType(getCellValueAsString(row.getCell(19)));
//            loanEntity.setRoi(Double.valueOf(getCellValueAsString(row.getCell(20))));
//            loanEntity.setRepayment(getCellValueAsString(row.getCell(21)));
//
//            // disbursedDate
//            String disbursedDateString = getCellValueAsString(row.getCell(22));
//            System.out.println("Disbursed Date: " + disbursedDateString); // Log cell value
//            if (isValidDate(disbursedDateString)) {
//                try {
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                    Date disbursedDate = dateFormat.parse(disbursedDateString);
//                    // Uncomment if you add disbursedDate field to LoanEntity
//                    // loanEntity.setDisbursedDate(disbursedDate);
//                } catch (ParseException e) {
//                    System.err.println("Failed to parse disbursed date: " + disbursedDateString);
//                    e.printStackTrace();
//                }
//            }
//
//            // Set remaining string fields
//            loanEntity.setCollectionDate(getCellValueAsString(row.getCell(23)));
//            loanEntity.setBounceCharge(getCellValueAsString(row.getCell(24)));
//            loanEntity.setPenalCharge(getCellValueAsString(row.getCell(25)));
//
//            // Set remaining numeric fields
//            loanEntity.setInterestPart(Double.valueOf(getCellValueAsString(row.getCell(26))));
//            loanEntity.setPrincipalPart(getCellValueAsString(row.getCell(27)));
//            loanEntity.setTotalPaid(getCellValueAsString(row.getCell(28)));
//
//            // Set created date
//            loanEntity.setCreatedDate(LocalDateTime.now());
//
//            loanEntities.add(loanEntity);
//        }
//
//        workbook.close();
//        return loanEntities;
//    }
//    private boolean isValidDate(String dateStr) {
//        if (dateStr == null || dateStr.isEmpty()) {
//            return false;
//        }
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            dateFormat.setLenient(false);
//            dateFormat.parse(dateStr);
//            return true;
//        } catch (ParseException e) {
//            return false;
//        }
//    }


//    private List<LoanEntity> readExcelFile(InputStream inputStream) throws IOException {
//        List<LoanEntity> loanEntities = new ArrayList<>();
//        Workbook workbook = new XSSFWorkbook(inputStream);
//        Sheet sheet = workbook.getSheetAt(0);
//
//        for (Row row : sheet) {
//            if (row.getRowNum() == 0) {
//                continue;
//            }
//            LoanEntity loanEntity = new LoanEntity();
//
//            String approvedDateString = getCellValueAsString(row.getCell(1));
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            try {
//                Date approvedDate = dateFormat.parse(approvedDateString);
//                loanEntity.setApprovedDate(approvedDate);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            loanEntity.setUtr(getCellValueAsString(row.getCell(2)));
//            loanEntity.setUmrnNumber(getCellValueAsString(row.getCell(3)));
//            loanEntity.setESign(getCellValueAsString(row.getCell(4)));
//            loanEntity.setPartnerLoanId(getCellValueAsString(row.getCell(5)));
//            loanEntity.setPartnerName(getCellValueAsString(row.getCell(6)));
//            loanEntity.setBranchName(getCellValueAsString(row.getCell(7)));
//            loanEntity.setBorrowerName(getCellValueAsString(row.getCell(8)));
//            loanEntity.setMobileNumber(getCellValueAsString(row.getCell(9)));
//            loanEntity.setEmailId(getCellValueAsString(row.getCell(10)));
//            loanEntity.setLoanType(getCellValueAsString(row.getCell(11)));
//            loanEntity.setLoanAmount(Double.valueOf(getCellValueAsString(row.getCell(12))));
//            loanEntity.setProcessingFeeCharge(getCellValueAsString(row.getCell(13)));
//            loanEntity.setProcessingFee(getCellValueAsString(row.getCell(14)));
//            loanEntity.setGst(getCellValueAsString(row.getCell(15)));
//            loanEntity.setDisbursedAmount(getCellValueAsString(row.getCell(16)));
//            loanEntity.setTenure(Integer.valueOf(getCellValueAsString(row.getCell(17))));
//            loanEntity.setNumberOfEmi(getCellValueAsString(row.getCell(18)));
//            loanEntity.setRoiType(getCellValueAsString(row.getCell(19)));
//            loanEntity.setRoi(Double.valueOf(getCellValueAsString(row.getCell(20))));
//            loanEntity.setRepayment(getCellValueAsString(row.getCell(21)));
//          //  loanEntity.setDisbursedDate(getCellValueAsString(row.getCell(24)));
//
//            String disbursedDateString = getCellValueAsString(row.getCell(23));
//            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
//            try {
//                Date disbursedDate = dateFormat1.parse(disbursedDateString);
//                loanEntity.setDisbursedDate(disbursedDate);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            loanEntity.setCollectionDate(getCellValueAsString(row.getCell(23)));
//            loanEntity.setBounceCharge(getCellValueAsString(row.getCell(24)));
//            loanEntity.setPenalCharge(getCellValueAsString(row.getCell(25)));
//            loanEntity.setInterestPart(Double.valueOf(getCellValueAsString(row.getCell(26))));
//            loanEntity.setPrincipalPart(getCellValueAsString(row.getCell(27)));
//            loanEntity.setTotalPaid(getCellValueAsString(row.getCell(28)));
//
//            loanEntity.setCreatedDate(LocalDateTime.now());
//
//            loanEntities.add(loanEntity);
//        }
//
//        workbook.close();
//        loanRepository.saveAll(loanEntities);
//        return loanEntities;
//    }
//
//    private boolean isValidDate(String dateStr) {
//        if (dateStr == null || dateStr.isEmpty()) {
//            return false;
//        }
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            dateFormat.setLenient(false);
//            dateFormat.parse(dateStr);
//            return true;
//        } catch (ParseException e) {
//            return false;
//        }
//    }




    public void addLoanData(LoanRequest loanRequest) {


        LoanEntity loanEntity = new LoanEntity();

        //  loanEntity.setDate(loanRequest.getDate());
        loanEntity.setApprovedDate(loanRequest.getApprovedDate());
        loanEntity.setUtr(loanRequest.getUtr());
        loanEntity.setUmrnNumber(loanRequest.getUmrnNumber());
        loanEntity.setESign(loanRequest.getESign());

         loanEntity.setPartnerLoanId(loanRequest.getPartnerLoanId());
        loanEntity.setPartnerName(loanRequest.getPartnerName());
        loanEntity.setBranchName(loanRequest.getBranchName());
        loanEntity.setBorrowerName(loanRequest.getBorrowerName());
        loanEntity.setMobileNumber(loanRequest.getMobileNumber());
        loanEntity.setEmailId(loanRequest.getEmailId());
        loanEntity.setLoanType(loanRequest.getLoanType());
        loanEntity.setLoanAmount(loanRequest.getLoanAmount());
        loanEntity.setProcessingFeeCharge(loanRequest.getProcessingFeeCharge());
        loanEntity.setProcessingFee(loanRequest.getProcessingFee());
        loanEntity.setGst(loanRequest.getGst());
        loanEntity.setDisbursedAmount(loanRequest.getDisbursedAmount());
        loanEntity.setTenure(loanRequest.getTenure());
        loanEntity.setNumberOfEmi(loanRequest.getNumberOfEmi());
        loanEntity.setRoiType(loanRequest.getRoiType());
        loanEntity.setRoi(loanRequest.getRoi());
        loanEntity.setRepayment(loanRequest.getRepayment());
        loanEntity.setDisbursedDate(loanRequest.getDisbursedDate());
       // loanEntity.setCollectionDate(loanRequest.getCollectionDate());
        loanEntity.setBounceCharge(loanRequest.getBounceCharge());
        loanEntity.setPenalCharge(loanRequest.getPenalCharge());
        loanEntity.setInterestPart(loanRequest.getInterestPart());
        loanEntity.setPrincipalPart(loanRequest.getPrincipalPart());
        loanEntity.setTotalPaid(loanRequest.getTotalPaid());
        loanEntity.setCreatedDate(LocalDateTime.now());

        loanRepository.save(loanEntity);


    }
    public void deleteLoanById(Long id)
    {
        loanRepository.deleteById(id);
    }


    public void updateLoanDetails(Long id, LoanRequest loanRequest) {
        Optional<LoanEntity> loan = loanRepository.findById(id);

        if (loan.isPresent()) {
            LoanEntity loanEntity = loan.get();
           // loanEntity.setDate(loanRequest.getDate());
            loanEntity.setApprovedDate(loanRequest.getApprovedDate());
            loanEntity.setUtr(loanRequest.getUtr());
            loanEntity.setUmrnNumber(loanRequest.getUmrnNumber());
            loanEntity.setESign(loanRequest.getESign());

             loanEntity.setPartnerLoanId(loanRequest.getPartnerLoanId());
            loanEntity.setPartnerName(loanRequest.getPartnerName());
            loanEntity.setBranchName(loanRequest.getBranchName());
            loanEntity.setBorrowerName(loanRequest.getBorrowerName());
            loanEntity.setMobileNumber(loanRequest.getMobileNumber());
            loanEntity.setEmailId(loanRequest.getEmailId());
            loanEntity.setLoanType(loanRequest.getLoanType());
            loanEntity.setLoanAmount(loanRequest.getLoanAmount());
            loanEntity.setProcessingFeeCharge(loanRequest.getProcessingFeeCharge());
            loanEntity.setProcessingFee(loanRequest.getProcessingFee());
            loanEntity.setGst(loanRequest.getGst());
            loanEntity.setDisbursedAmount(loanRequest.getDisbursedAmount());
            loanEntity.setTenure(loanRequest.getTenure());
            loanEntity.setNumberOfEmi(loanRequest.getNumberOfEmi());
            loanEntity.setRoiType(loanRequest.getRoiType());
            loanEntity.setRoi(loanRequest.getRoi());
            loanEntity.setRepayment(loanRequest.getRepayment());
            loanEntity.setDisbursedDate(loanRequest.getDisbursedDate());
           loanEntity.setCollectionDate(loanRequest.getCollectionDate());
            loanEntity.setBounceCharge(loanRequest.getBounceCharge());
            loanEntity.setPenalCharge(loanRequest.getPenalCharge());
            loanEntity.setInterestPart(loanRequest.getInterestPart());
            loanEntity.setPrincipalPart(loanRequest.getPrincipalPart());
            loanEntity.setTotalPaid(loanRequest.getTotalPaid());


            loanRepository.save(loanEntity);
        } else {
            throw new NullPointerException(" Loan Details not found with id: " + id);
        }

    }

    public List<LoanEntity> getAllLoan() {

        return loanRepository.findAll();
    }

    public byte[] exportAllLoanDetailsToExcel(List<LoanEntity> loanDetails) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet("Loan Details");
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Date", "Approved Date", "UTR", "UMRN Number", "eSign", "Loan ID",
                "Partner Loan ID", "Partner Name", "Branch Name", "Borrower Name",
                "Mobile Number", "Email ID", "Loan Type", "Loan Amount",
                "Processing Fee Charge", "Processing Fee", "GST", "Disbursed Amount",
                "Tenure", "Number of EMI", "ROI Type", "ROI", "Repayment",
                "Disbursed Date", "Collection Date", "Bounce Charge", "Penal Charge",
                "Interest Part", "Principal Part", "Total Paid"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (LoanEntity loan : loanDetails) {
            Row dataRow = sheet.createRow(rowNum++);

           // dataRow.createCell(0).setCellValue(loan.getDate() != null ? loan.getDate().toString() : "");
            dataRow.createCell(1).setCellValue(loan.getApprovedDate() != null ? loan.getApprovedDate().toString() : "");
            dataRow.createCell(2).setCellValue(loan.getUtr());
            dataRow.createCell(3).setCellValue(loan.getUmrnNumber());
            dataRow.createCell(4).setCellValue(loan.getESign());

             dataRow.createCell(6).setCellValue(loan.getPartnerLoanId());
            dataRow.createCell(7).setCellValue(loan.getPartnerName());
            dataRow.createCell(8).setCellValue(loan.getBranchName());
            dataRow.createCell(9).setCellValue(loan.getBorrowerName());
            dataRow.createCell(10).setCellValue(loan.getMobileNumber());
            dataRow.createCell(11).setCellValue(loan.getEmailId());
            dataRow.createCell(12).setCellValue(loan.getLoanType());
            dataRow.createCell(13).setCellValue(loan.getLoanAmount());
            dataRow.createCell(14).setCellValue(loan.getProcessingFeeCharge());
            dataRow.createCell(15).setCellValue(loan.getProcessingFee());
            dataRow.createCell(16).setCellValue(loan.getGst());
            dataRow.createCell(17).setCellValue(loan.getDisbursedAmount());
            dataRow.createCell(18).setCellValue(loan.getTenure());
            dataRow.createCell(19).setCellValue(loan.getNumberOfEmi());
            dataRow.createCell(20).setCellValue(loan.getRoiType());
            dataRow.createCell(21).setCellValue(loan.getRoi());
            dataRow.createCell(22).setCellValue(loan.getRepayment());
         //   dataRow.createCell(23).setCellValue(loan.getDisbursedDate());
          //  dataRow.createCell(24).setCellValue(loan.getCollectionDate());
            dataRow.createCell(25).setCellValue(loan.getBounceCharge());
            dataRow.createCell(26).setCellValue(loan.getPenalCharge());
            dataRow.createCell(27).setCellValue(loan.getInterestPart());
            dataRow.createCell(28).setCellValue(loan.getPrincipalPart());
            dataRow.createCell(29).setCellValue(loan.getTotalPaid());
        }

        workbook.write(byteArrayOutputStream);
        workbook.close();
        return byteArrayOutputStream.toByteArray();
    }





    public List<LoanEntity> searchByBorrowerName(String borrowerName) {
        return loanRepository.findByBorrowerName(borrowerName);
    }

    public List<LoanEntity> searchByMobileNumber(String mobileNumber) {
        return loanRepository.findByMobileNumber(mobileNumber);
    }


    public LoanRequest getLoanById(Long loanId) {
        LoanEntity loanEntity = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("loanId not found with ID: " + loanId));

        LoanRequest loan = new LoanRequest();
        loan.setId(loanEntity.getId());
       // loan.setDate(loanEntity.getDate());
        loan.setApprovedDate(loanEntity.getApprovedDate());
        loan.setUtr(loanEntity.getUtr());
        loan.setUmrnNumber(loanEntity.getUmrnNumber());
        loan.setESign(loanEntity.getESign());
        loan.setPartnerLoanId(loanEntity.getPartnerLoanId());
        loan.setPartnerName(loanEntity.getPartnerName());
        loan.setBranchName(loanEntity.getBranchName());
        loan.setBorrowerName(loanEntity.getBorrowerName());
        loan.setMobileNumber(loanEntity.getMobileNumber());
        loan.setEmailId(loanEntity.getEmailId());
        loan.setLoanType(loanEntity.getLoanType());
        loan.setLoanAmount(loanEntity.getLoanAmount());
        loan.setProcessingFeeCharge(loanEntity.getProcessingFeeCharge());
        loan.setProcessingFee(loanEntity.getProcessingFee());
        loan.setGst(loanEntity.getGst());
        loan.setDisbursedAmount(loanEntity.getDisbursedAmount());
        loan.setTenure(loanEntity.getTenure());
        loan.setNumberOfEmi(loanEntity.getNumberOfEmi());
        loan.setRoiType(loanEntity.getRoiType());
        loan.setRoi(loanEntity.getRoi());
        loan.setRepayment(loanEntity.getRepayment());
        loan.setDisbursedDate(loanEntity.getDisbursedDate());
        loan.setCollectionDate(loanEntity.getCollectionDate());
        loan.setBounceCharge(loanEntity.getBounceCharge());
        loan.setPenalCharge(loanEntity.getPenalCharge());
        loan.setInterestPart(loanEntity.getInterestPart());
        loan.setPrincipalPart(loanEntity.getPrincipalPart());
        loan.setTotalPaid(loanEntity.getTotalPaid());
        loan.setCreatedDate(LocalDateTime.now());


        return loan;

    }
    public LoanRequest getByCustomerId(Long customerId) {
        LoanEntity loanEntity = loanRepository.findByCustomerId(String.valueOf(customerId))
                .orElseThrow(() -> new RuntimeException("loanId not found with ID: " + customerId));

        LoanRequest loan = new LoanRequest();
        loan.setId(loanEntity.getId());
        loan.setCustomerId(loanEntity.getCustomerId());
        // loan.setDate(loanEntity.getDate());
        loan.setApprovedDate(loanEntity.getApprovedDate());
        loan.setUtr(loanEntity.getUtr());
        loan.setUmrnNumber(loanEntity.getUmrnNumber());
        loan.setESign(loanEntity.getESign());
        loan.setPartnerLoanId(loanEntity.getPartnerLoanId());
        loan.setPartnerName(loanEntity.getPartnerName());
        loan.setBranchName(loanEntity.getBranchName());
        loan.setBorrowerName(loanEntity.getBorrowerName());
        loan.setMobileNumber(loanEntity.getMobileNumber());
        loan.setEmailId(loanEntity.getEmailId());
        loan.setLoanType(loanEntity.getLoanType());
        loan.setLoanAmount(loanEntity.getLoanAmount());
        loan.setProcessingFeeCharge(loanEntity.getProcessingFeeCharge());
        loan.setProcessingFee(loanEntity.getProcessingFee());
        loan.setGst(loanEntity.getGst());
        loan.setDisbursedAmount(loanEntity.getDisbursedAmount());
        loan.setTenure(loanEntity.getTenure());
        loan.setNumberOfEmi(loanEntity.getNumberOfEmi());
        loan.setRoiType(loanEntity.getRoiType());
        loan.setRoi(loanEntity.getRoi());
        loan.setRepayment(loanEntity.getRepayment());
        loan.setDisbursedDate(loanEntity.getDisbursedDate());
        loan.setCollectionDate(loanEntity.getCollectionDate());
        loan.setBounceCharge(loanEntity.getBounceCharge());
        loan.setPenalCharge(loanEntity.getPenalCharge());
        loan.setInterestPart(loanEntity.getInterestPart());
        loan.setPrincipalPart(loanEntity.getPrincipalPart());
        loan.setTotalPaid(loanEntity.getTotalPaid());
        loan.setCreatedDate(LocalDateTime.now());


        return loan;

    }


    @Transactional
    public CustomerVerified verifyLoan(Long loanId) {
        // Fetch loan details from LoanEntity
        LoanEntity loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        // Calculate processing fee and net disbursed amount
        double processingFee = loan.getLoanAmount() * 0.04;
        double netDisbursedAmount = loan.getLoanAmount() - processingFee;

        // Populate the CustomerVerified entity
        CustomerVerified customerVerified = new CustomerVerified();
        customerVerified.setCustomerId(loan.getId());
        customerVerified.setPartnerLoanId(loan.getPartnerLoanId());
        customerVerified.setBorrowerName(loan.getBorrowerName());
        customerVerified.setMobileNumber(loan.getMobileNumber());
        customerVerified.setEmail(loan.getEmailId());

        // Set loan details and calculated values
        customerVerified.setLoanAmount(loan.getLoanAmount()); // Original loan amount
        customerVerified.setProcessingFee(String.valueOf(processingFee));
        customerVerified.setTotalPaid(loan.getLoanAmount());
        customerVerified.setPrincipalPart(loan.getLoanAmount());
        customerVerified.setEmiAmount(loan.getLoanAmount());

        // Default values
        customerVerified.setNumberOfEmi("1");
        customerVerified.setTenure("45");

        // Set dates to current datetime
        customerVerified.setApprovedDate(LocalDateTime.now());
        customerVerified.setDisbursedDate(LocalDateTime.now());
        customerVerified.setCollectionDate(LocalDateTime.now());

        // Disbursed amount after processing fee
        customerVerified.setDisbursedAmount(String.valueOf(netDisbursedAmount));
        customerVerified.setStatus("Verified");

        // Save the verification details to the repository
        return customerVerifiedRepository.save(customerVerified);
    }



}