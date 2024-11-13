package com.flik.controller;

import com.flik.Service.CustomerServices;
import com.flik.entity.CustomerEntity;
import com.flik.model.CustomerRequestModel;
import com.flik.request.CustomerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/customer")

public class CustomerController {

    @Autowired
    private CustomerServices customerService;
//    @Autowired
//    private CustomerService customerServices;

    @PostMapping("/upload")
    public ResponseEntity<List<CustomerEntity>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            List<CustomerEntity> customers = customerService.previewCustomers(file);
            return ResponseEntity.ok(customers);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }




    @PostMapping("/save")
    public ResponseEntity<String> saveCustomers(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a file!");
        }
        try {
            customerService.save(file);
            return ResponseEntity.ok("File uploaded and data saved successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());

        }
    }





    @PostMapping("/addCustomerData")
    public ResponseEntity<String> addCustomerData(@RequestBody CustomerRequest customerRequest) {

        try {
            customerService.addCustomerData(customerRequest);
            return new ResponseEntity<>("User Added Successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add Customer Details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("customerDelete")
    public ResponseEntity<HttpStatus>  deleteCustomerById(@RequestParam("id") Long id  ) {
        try {
            customerService.deleteCustomerById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }


    @PutMapping("updateCustomer")
    public ResponseEntity<String> updateCustomer(@RequestParam("id") Long id, @RequestBody CustomerRequest customerRequest) {
        try {
            customerService.updateCustomerDetails(id, customerRequest);
            return ResponseEntity.ok("Customers updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
        @GetMapping("/excelCustomerList")
        public List<CustomerEntity> getAllCustomerDetails() {

            return  customerService.getAllCustomer();
        }


    @GetMapping("/customerToExport")
    public ResponseEntity<?> exportCustomerToExcel() {
        List<CustomerEntity> customerEntities = customerService.getAllCustomer();
        String fileName = "FlikCustomerExcel.xlsx";

        try {
            if (customerEntities != null && !customerEntities.isEmpty()) {
                byte[] excelContent = customerService.exportAllCustomerDetailsToExcel(customerEntities);
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






    @GetMapping("/searchByBorrowerName")
    public List<CustomerEntity> searchByBorrowerName(@RequestParam("borrowerName") String borrowerName) {
        return customerService.searchByBorrowerName(borrowerName);
    }

    @GetMapping("/searchByMobileNumber")
    public List<CustomerEntity> searchByMobileNumber(@RequestParam("mobileNumber") String mobileNumber) {
        return customerService.searchByMobileNumber(mobileNumber);
    }

    @GetMapping("/customerListById")
    public CustomerRequestModel
    getCustomerDetailsById(@RequestParam("id") Long customerId) {

        return customerService.getCustomerDetailsById(customerId);

    }
//    @GetMapping("/customers")
//    public ResponseEntity<List<CustomerEntity>> getCustomersByUserFintechId( @RequestParam("userFintechId") String userFintechId) {
//        List<CustomerEntity> customers = customerService.getCustomersByUserFintechId(userFintechId);
//        if (customers.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(customers);
//    }

    @PutMapping("/kyc")
    public ResponseEntity<String> completeKyc(@RequestParam("id") Long customerId) {
        String result = customerService.completeKyc(customerId);
        if ("KYC completed successfully.".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }




    @GetMapping("/customerStatus")
    public List<CustomerEntity> getNewCustomersByStatus(@RequestParam("status") String status) {
        return customerService.getNewCustomersByStatus(status);
    }

//    @GetMapping("/fintechId")
//    public List<CustomerEntity> getByFintechId(@RequestParam("fintechId") String fintechId) {
//        return customerService.getByFintechId(fintechId);
//    }




    @GetMapping("/countTotalBorrower")
    public long countTotalBorrower() {
        return customerService.countTotalBorrower();
    }


    @GetMapping("/sample")
    public ResponseEntity<Resource> downloadSampleCustomerExcel() throws IOException {
        Resource resource = customerService.getSampleCustomerExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=CustomerDatasample.xlsx");
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .body(resource);
    }










}


