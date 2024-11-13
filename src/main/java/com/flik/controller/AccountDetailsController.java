package com.flik.controller;

import com.flik.Service.AccountDetailsService;
import com.flik.entity.CustomerVerified;
import com.flik.global.APIGateWay;
import com.flik.model.AccountNumberModel;
import com.flik.model.CustomerVerifiedModel;
import com.flik.repository.AdminRepository;
import com.flik.repository.CustomerVerifiedRepository;
import com.flik.respons.CustomerVerifiedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountDetailsController {

    @Autowired
    private APIGateWay gateWay;

    @Autowired
    private AccountDetailsService accountDetailsService;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private CustomerVerifiedRepository customerVerifiedRepository;



    @PostMapping("/saveAccountNum")
    public String account(@RequestBody AccountNumberModel accountNumRequest, @RequestParam("id") Long customerId ){

        try {
                return accountDetailsService.accountDetails( accountNumRequest, customerId);
    } catch (Exception e) {
        e.printStackTrace();
                 return "Error during credit check";
    }

  }

    @PostMapping("/verifiedCustomer")
    public ResponseEntity<String> transferCustomerToVerified(@RequestParam("id") Long customerId) {
        String resultMessage = accountDetailsService.transferToVerified(customerId);
        return ResponseEntity.ok(resultMessage);
    }

    @GetMapping("/verifiedCustomerList")
    public List<CustomerVerified> verifiedCustomerList() {

        return  accountDetailsService.verifiedCustomerList();
    }
    @GetMapping("/customerVerifiedToExport")
    public ResponseEntity<?> exportCustomerVerifiedToExcel() {
        List<CustomerVerified> customerEntities = accountDetailsService.verifiedCustomerList();
        String fileName = "FlikVerifiedCustomerExcel.xlsx";

        try {
            if (customerEntities != null && !customerEntities.isEmpty()) {
                byte[] excelContent = accountDetailsService.exportAllCustomerDetailsToExcel(customerEntities);
                ByteArrayResource resource = new ByteArrayResource(excelContent);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .contentLength(resource.contentLength())
                        .body(resource);

            } else {
                return new ResponseEntity<>("No Customer found to export", HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to export Customer to Excel", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/ifscCode")
    public ResponseEntity<?> ifsccode(@RequestParam String ifsccode, @RequestParam("id") Long customerId) {
        return gateWay.handleRequest(accountDetailsService.getIfscDetails(ifsccode, customerId));

    }



    @GetMapping("/verifiedCustomerById")
    public CustomerVerifiedResponse getVerifiedCustomerById(@RequestParam("id") Long customerId) {

        return accountDetailsService.getVerifiedCustomerById(customerId);

    }

    @GetMapping("/countTotalActiveBorrowers")
    public long countActiveBorrowers() {
        return accountDetailsService.countActiveBorrowers();
    }

    @GetMapping("/countTotalClosedBorrowers")
    public long countClosedAccounts() {
        return accountDetailsService.countClosedAccounts();
    }




    @DeleteMapping("/deleteVerifiedCustomer")
    public ResponseEntity<String> deleteCustomerById(@RequestParam("id") Long customerId, @RequestParam("password") String password)
    {
        try{
            accountDetailsService.deleteVerifiedCustomerByCustomerId(customerId, password);
            return ResponseEntity.ok("Customer Deleted successfully");
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
    @PutMapping("/statusDisbursed")
    public ResponseEntity<String> statusDisbursed(@RequestParam("id") Long customerId) {
        String result = accountDetailsService.statusDisbursed(customerId);
        if ("Disbursed completed successfully.".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }


    @PutMapping("updateReason")
    public ResponseEntity<String> updateTutorial(@RequestParam("id") Long customerId, @RequestBody CustomerVerifiedModel customerVerifiedModel) {
        try {
            accountDetailsService.updateEmployeePersonalDetails(customerId, customerVerifiedModel);
            return ResponseEntity.ok("Verified Customers  updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/customerVerifiedStatus")
    public List<CustomerVerified> getNewCustomersByStatus(@RequestParam("status") String status) {
        return accountDetailsService.getNewCustomersByStatus(status);
    }




    @GetMapping("/verifiedSearchByBorrowerName")
    public List<CustomerVerified> verifiedSearchByBorrowerName(@RequestParam("borrowerName") String borrowerName) {
        return accountDetailsService.verifiedSearchByBorrowerName(borrowerName);
    }

    @GetMapping("/verifiedSearchByMobileNumber")
    public List<CustomerVerified> verifiedSearchByMobileNumber(@RequestParam("mobileNumber") String mobileNumber) {
        return accountDetailsService.verifiedSearchByMobileNumber(mobileNumber);
    }

//
//    @PostMapping("/loanDeduct")
//    public String verifyCustomer(@RequestParam Long loanId) {
//        accountDetailsService.verifyCustomer(loanId);
//        return "Customer verified, and processing fee deducted from loan amount.";
//    }





}
