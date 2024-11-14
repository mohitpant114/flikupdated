package com.flik.repository;

import com.flik.entity.CustomerEntity;
import com.flik.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<LoanEntity , Long> {
   // List<LoanEntity> findByLoanId(String borrowerId);
    List<LoanEntity> findByBorrowerName(String borrowerName);
    List<LoanEntity> findByMobileNumber(String mobileNumber);



    Optional<LoanEntity> findByCustomerId(String customerId);

    List<LoanEntity> findByStatus(String status);

}
