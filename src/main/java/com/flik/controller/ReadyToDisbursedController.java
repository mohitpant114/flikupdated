package com.flik.controller;

import com.flik.Service.ReadyToDisbursedService;
import com.flik.entity.ReadyToDisbursedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/readyToDisbursed")
public class ReadyToDisbursedController {

    @Autowired
    private ReadyToDisbursedService readyToDisbursedService;


    @PostMapping("/addLoanData")

    public ReadyToDisbursedEntity createReadyToDisbursement(@RequestParam Long customerId, @RequestParam Long loanId) {

    return readyToDisbursedService.createReadyToDisbursement(customerId, loanId);
}
}
