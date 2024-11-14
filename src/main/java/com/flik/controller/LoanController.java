package com.flik.controller;

import com.flik.Service.LoanService;
import com.flik.entity.CustomerVerified;
import com.flik.entity.LoanEntity;
import com.flik.request.LoanRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/loan")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @PostMapping("/uploadExcel")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a file!");
        }

        try {
            loanService.save(file);
            return ResponseEntity.ok("File uploaded and data saved successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }
    @PostMapping("/addLoanData")
    public ResponseEntity<String> addLoanData(@RequestBody LoanRequest loanRequest) {

        try {
            loanService.addLoanData(loanRequest);
            return new ResponseEntity<>("loan Added Successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add loan Details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("loanDelete")
    public ResponseEntity<HttpStatus>  deleteLoanById(@RequestParam("id") Long id  ) {
        try {
            loanService.deleteLoanById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("updateLoanDetails")
    public ResponseEntity<String> updateTutorial(@RequestParam("id") Long id, @RequestBody LoanRequest loanRequest) {
        try {
            loanService.updateLoanDetails(id, loanRequest);
            return ResponseEntity.ok("Loan updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/loanList")
    public List<LoanEntity> getAllLoanDetails() {

        return  loanService.getAllLoan();
    }


    @GetMapping("/loanToExport")
    public ResponseEntity<?> exportLoanToExcel() {
        List<LoanEntity> loan = loanService.getAllLoan();
        String fileName = "FlikLoanExcel.xlsx";

        try {
            if (loan != null && !loan.isEmpty()) {
                byte[] excelContent = loanService.exportAllLoanDetailsToExcel(loan);
                ByteArrayResource resource = new ByteArrayResource(excelContent);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .contentLength(resource.contentLength())
                        .body(resource);

            } else {
                return new ResponseEntity<>("No Loan Details found to export", HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to export Loan Details to Excel", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }







        @GetMapping("/searchByLoanId")
        public LoanRequest getLoanById(@RequestParam("id") Long loanId) {

                return loanService.getLoanById(loanId);

}


    @GetMapping("/searchByCustomerId")
    public LoanRequest getByLoanId(@RequestParam("id") Long customerId) {

        return loanService.getByCustomerId(customerId);

    }


    @GetMapping("/searchByBorrowerName")
    public List<LoanEntity> searchByBorrowerName(@RequestParam("borrowerName") String borrowerName) {
        return loanService.searchByBorrowerName(borrowerName);
    }

    @GetMapping("/searchByMobileNumber")
    public List<LoanEntity> searchByMobileNumber(@RequestParam("mobileNumber") String mobileNumber) {
        return loanService.searchByMobileNumber(mobileNumber);
    }

//    @PostMapping("/loanDeduct")
//    public CustomerVerified verifyLoan(@RequestParam("id") Long loanId) {
//        return loanService.verifyLoan(loanId);
//    }

    @GetMapping("/verifiedLoans")
    public ResponseEntity<List<LoanEntity>> getVerifiedLoans() {
        try {
            List<LoanEntity> verifiedLoans = loanService.getVerifiedLoans();
            if (verifiedLoans.isEmpty()) {
                return ResponseEntity.noContent().build(); // Return 204 if no verified loans are found
            }
            return ResponseEntity.ok(verifiedLoans); // Return 200 and the list of loans
        } catch (Exception e) {
            // Log the exception and return 500 Internal Server Error
            return ResponseEntity.status(500).body(null);
        }
    }







}
