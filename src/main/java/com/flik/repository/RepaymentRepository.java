package com.flik.repository;

import com.flik.entity.LoanEntity;
import com.flik.entity.RepaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RepaymentRepository extends JpaRepository<RepaymentEntity , Long> {
    List<RepaymentEntity> findByBorrowerName(String borrowerName);
    List<RepaymentEntity> findByLoanId(Long loanId);
    List<RepaymentEntity> findByStatus(String status);
   // List<RepaymentEntity> findByLoanIdAndStatus(Long loanId, String status);
   boolean existsByLoanId(Long loanId);
    List<RepaymentEntity> findByDueDate(LocalDate dueDate);


    List<RepaymentEntity> findByCustomerId(String customerId);

}
