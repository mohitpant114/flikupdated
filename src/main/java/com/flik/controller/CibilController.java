package com.flik.controller;

import com.flik.Service.CibilService;
import com.flik.entity.RejectCustomerEntity;
import com.flik.model.CibilModel;
import com.flik.respons.RejectCustomerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/cibil")
public class CibilController {
    @Autowired
    private CibilService cibilService;

    private static final Logger logger = LoggerFactory.getLogger(CibilController.class);


    @PostMapping("/create")
    public String createCibilRequest( @RequestBody CibilModel cibilModel, @RequestParam("id") Long customerId) {
        try {
            return cibilService.createAndPostCibilRequest( cibilModel, customerId);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error during credit check";
        }
    }


    @GetMapping("/rejectListById")
    public RejectCustomerResponse getCustomerRejectByCustomerId(@RequestParam("id") Long rejectId) {

        return cibilService.getRejectListById(rejectId);

    }

    @GetMapping("/rejectList")
    public List<RejectCustomerEntity> getAllRejectList() {

        return  cibilService.getAllRejectList();
    }
    @GetMapping("/rejectStatus")
    public List<RejectCustomerEntity> getCustomersByStatus(@RequestParam("status") String status) {
        return cibilService.getCustomersByStatus(status);
    }

    @GetMapping("/countTotalReject")
    public long countTotalReject() {
        return cibilService.countTotalReject();
    }








    @GetMapping("/download-pdf")
    public ResponseEntity<InputStreamResource> downloadPdf(@RequestParam("customerId") Long customerId) {

        byte[] pdfBytes = cibilService.getPdf(customerId);


        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"document_" + customerId + ".pdf\"");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(byteArrayInputStream));
    }



}
