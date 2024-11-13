package com.flik.controller;

import com.flik.Service.RepaymentService;
import com.flik.entity.LoanEntity;
import com.flik.entity.OverDueEntity;
import com.flik.entity.RepaymentEntity;
import com.flik.request.RepaymentRequest;
import com.flik.respons.DueEmiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/repayment")
public class RepaymentController {

    @Autowired
    private RepaymentService repaymentService;



    @PostMapping("/createFlatLoanId")
    public ResponseEntity<String> createFlatRepayments(@RequestParam("loanId") Long loanId) {
        try{
            if(repaymentService.repaymentsExist(loanId)){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Repayment table already exists for loan id: " + loanId);
            }
            repaymentService.createFlatRepayments(loanId);
            return ResponseEntity.ok("Flat Repayment table created successfully for loan id: " + loanId);
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create Flat Repayment table for loan id: " + loanId);
        }
    }

    @PostMapping("/createReduceLoanId")
    public ResponseEntity<String> createReducingRepayments(@RequestParam("loanId") Long loanId) {
        try {
            if (repaymentService.repaymentsExist(loanId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Repayment table already exists for loan id: " + loanId);
            }
            repaymentService.createReducingRepayments(loanId);
            return ResponseEntity.ok("Reducing Repayment table created successfully for loan id: " + loanId);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create Reducing Repayment table for loan id: " + loanId);
        }
    }

    @PostMapping("/uploadExcel")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a file!");
        }

        try {
            repaymentService.save(file);
            return ResponseEntity.ok("File uploaded and data saved successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

//    @PostMapping("/createFlatLoanId")
//    public ResponseEntity<String> createRepayments(@RequestParam("id") Long loanId) {
//        try {
//            if (repaymentService.repaymentsExist(loanId)) {
//
//                return ResponseEntity.status(HttpStatus.CONFLICT)
//                        .body("Repayment table already exists for loan id: " + loanId);
//            }
//            repaymentService.createReducingRepayments(loanId );
//            return ResponseEntity.ok("Repayment table created successfully for loan id: " + loanId);
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to Create Repayment Table for loan id: " + loanId);
//        }
//    }
//    @PostMapping("/createReduceLoanId")
//    public ResponseEntity<String> calculateReducingRepayments(@RequestParam("id") Long loanId) {
//        try {
//            if (repaymentService.repaymentsExist(loanId)) {
//
//                return ResponseEntity.status(HttpStatus.CONFLICT)
//                        .body("Repayment table already exists for loan id: " + loanId);
//            }
//
//            repaymentService.createFlatRepayments(loanId);
//            return ResponseEntity.ok("Repayment table created successfully for loan id: " + loanId);
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to Create Repayment Table for loan id: " + loanId);
//        }
//    }
//


    @GetMapping("/repaymentListByCustomerId")
    public ResponseEntity<List<RepaymentEntity>> getRepaymentsByCustomerId(@RequestParam("id")  String customerId) {
        try {
            List<RepaymentEntity> repayments = repaymentService.getRepaymentsByCustomerId(customerId);
            return ResponseEntity.ok(repayments);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }


    @GetMapping("/listByLoanId")
    public ResponseEntity<List<RepaymentEntity>> getRepaymentsByLoanId(@RequestParam("loanId") Long loanId) {
        try {
            List<RepaymentEntity> repayments = repaymentService.getRepaymentsByLoanId(loanId);
            return ResponseEntity.ok(repayments);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }


//List Excel

    @GetMapping("/dueToday")
    public Map<String, List<RepaymentEntity>> getDueEmisForToday() {
        List<RepaymentEntity> dueEmis = repaymentService.getDueEmisForToday();
        Map<String, List<RepaymentEntity>> response = new HashMap<>();
        response.put("dueEmis", dueEmis);
        return response;
    } // due Emi Excel



    @PutMapping("/status")
    public ResponseEntity<String> markRepaymentAsPaid(@RequestParam("id") Long repaymentId, @RequestParam ("status") String status) {
        repaymentService.updateRepaymentStatus(repaymentId, status);

        String message;
        if (status.equalsIgnoreCase("Paid")) {
            message = "Repayment marked as paid.";
        } else if (status.equalsIgnoreCase("Pending")) {
            message = "Repayment marked as pending.";
        } else {
            message = "Invalid status provided.";
        }

        return ResponseEntity.ok(message);

    }

    @GetMapping("/searchByBorrowerName")
    public List<RepaymentEntity> searchByBorrowerName(@RequestParam("borrowerName") String borrowerName) {
        return repaymentService.searchByBorrowerName(borrowerName);
    }

    @GetMapping("/searchByMobileNumber")
    public List<RepaymentEntity> searchByLoanId(@RequestParam("MobileNumber") Long mobileNumber) {
        return repaymentService.searchByMobileNumber(mobileNumber);
    }



    @GetMapping("/searchByRepaymentId")
    public RepaymentRequest getRepaymentById(@RequestParam("id") Long repaymentId) {

        return repaymentService.getRepaymentById(repaymentId);

    }

    //List for all RepaymentTable And Export the Repayment Table


    @GetMapping("/repaymentList")
    public List<RepaymentEntity> getAllRepaymentDetails() {

        return  repaymentService.getAllRepaymentList();
    }

    @GetMapping("/repaymentToExport")
    public ResponseEntity<?> exportLoanToExcel() {
        List<RepaymentEntity> repaymentEntities = repaymentService.getAllRepaymentList();
        String fileName = "FLikAllRepaymentExcel.xlsx";

        try {
            if (repaymentEntities != null && !repaymentEntities.isEmpty()) {
                byte[] excelContent = repaymentService.exportAllRepaymentDetailsToExcel(repaymentEntities);
                ByteArrayResource resource = new ByteArrayResource(excelContent);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .contentLength(resource.contentLength())
                        .body(resource);

            } else {
                return new ResponseEntity<>("No Repayment Details found to export", HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to export Repayment Details to Excel", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/overDueList")
    public List<OverDueEntity> getAllOverDueDetails() {

        return  repaymentService.getAllOverDueList();
    }
    @GetMapping("/overDueToExport")
    public ResponseEntity<?> exportOverDueToExcel() {
        List<OverDueEntity> overDueEntities = repaymentService.getAllOverDueList();
        String fileName = "FlikAllOverDueListExcel.xlsx";

        try {
            if (overDueEntities != null && !overDueEntities.isEmpty()) {
                byte[] excelContent = repaymentService.exportAllOverdueDetailsToExcel(overDueEntities);
                ByteArrayResource resource = new ByteArrayResource(excelContent);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .contentLength(resource.contentLength())
                        .body(resource);

            } else {
                return new ResponseEntity<>("No overDue Details found to export", HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to export overDue Details to Excel", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }






//    @GetMapping("/checkOverdue")
//    public ResponseEntity<String> checkAndHandleOverduePayments() {
//        repaymentService.checkAndHandleOverduePayments();
//        return ResponseEntity.ok("Overdue payments checked and handled.");
//    }


//    @GetMapping("/countPaidEmis/{loanId} ")
//    public ResponseEntity<Integer> countPaidEmis(@PathVariable Long loanId) {
//        int paidEmisCount = repaymentService.getRepaymentsByLoanIdAndStatus(loanId);
//        return ResponseEntity.ok(paidEmisCount);
//    }
//    @GetMapping("/countPaidEmis")
//    public ResponseEntity<List<RepaymentEntity>> getRepaymentsByLoanIdAndStatus(
//            @RequestParam Long loanId,
//            @RequestParam String status) {
//
//        List<RepaymentEntity> repayments = repaymentService.getRepaymentsByLoanIdAndStatus(loanId, status);
//        return ResponseEntity.ok(repayments);
//    }





// OverDue














}
