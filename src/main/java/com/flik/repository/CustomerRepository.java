package com.flik.repository;

import com.flik.entity.AccountDetailsEntity;
import com.flik.entity.CustomerEntity;
import com.flik.entity.RejectCustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity , Long>
{

   // List<CustomerEntity> findByBorrowerId(String borrowerId);
    List<CustomerEntity> findByBorrowerName(String borrowerName);
    List<CustomerEntity> findByMobileNumber(String mobileNumber);

//    List<CustomerEntity> findByFintechId(String fintechId);

    List<CustomerEntity> findByStatus(String status);
    Optional<CustomerEntity> findByIdAndStatus(Long id, String status);

    Optional<CustomerEntity> findById(Long id);




}
