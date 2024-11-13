package com.flik.Service;

import com.flik.entity.CustomerEntity;
import com.flik.entity.LoanEntity;
import com.flik.entity.ReadyToDisbursedEntity;
import com.flik.repository.CustomerRepository;
import com.flik.repository.LoanRepository;
import com.flik.repository.ReadyToDisbursedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class ReadyToDisbursedService {


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ReadyToDisbursedRepository readyToDisbursedRepository;



    public ReadyToDisbursedEntity createReadyToDisbursement(Long customerId, Long loanId) {
        Optional<CustomerEntity> customerOpt = customerRepository.findById(customerId);
        Optional<LoanEntity> loanOpt = loanRepository.findById(loanId);

        if (customerOpt.isPresent() && loanOpt.isPresent()) {
            CustomerEntity customer = customerOpt.get();
            LoanEntity loan = loanOpt.get();

            ReadyToDisbursedEntity readyToDisbursed = new ReadyToDisbursedEntity();
            readyToDisbursed.setCustomerId(customer.getId());
            readyToDisbursed.setLoanId(loan.getId());
            readyToDisbursed.setPartnerLoanId(loan.getPartnerLoanId());
            readyToDisbursed.setPartnerName(loan.getPartnerName());
            readyToDisbursed.setProductName(loan.getLoanType());
            readyToDisbursed.setLoanAmount(loan.getLoanAmount());
            readyToDisbursed.setBorrowerName(customer.getBorrowerName());
            readyToDisbursed.setAccountNumber(customer.getAccountNumber());
            readyToDisbursed.setIfscCode(customer.getIfscCode());
            readyToDisbursed.setBankName(customer.getBankName());
            readyToDisbursed.setMobileNumber(customer.getMobileNumber());
            readyToDisbursed.setDisbursedAmount(loan.getDisbursedAmount());
            readyToDisbursed.setProcessingFee(loan.getProcessingFee());
            readyToDisbursed.setGst(loan.getGst());
            readyToDisbursed.setStatus(loan.getStatus());
            readyToDisbursed.setCreatedDate(LocalDateTime.now());
            readyToDisbursed.setApprovedDate(loan.getApprovedDate());

            return readyToDisbursedRepository.save(readyToDisbursed);

        }

        throw new RuntimeException("Customer or Loan not found");

    }



}
