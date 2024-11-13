package com.flik.repository;

import com.flik.entity.CustomerEntity;
import com.flik.entity.CustomerVerified;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerVerifiedRepository extends JpaRepository<CustomerVerified , Long> {
    Optional<CustomerVerified> findByCustomerId(Long customerId);
    long countByStatus(String status);
    List<CustomerVerified> findByStatus(String status);
    List<CustomerVerified> findByBorrowerName(String borrowerName);
    List<CustomerVerified> findByMobileNumber(String mobileNumber);
}
