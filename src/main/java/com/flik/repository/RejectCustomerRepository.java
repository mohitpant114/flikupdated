package com.flik.repository;

import com.flik.entity.CustomerVerified;
import com.flik.entity.RejectCustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RejectCustomerRepository extends JpaRepository<RejectCustomerEntity , Long> {
    List<RejectCustomerEntity> findByStatus(String status);
    Optional<RejectCustomerEntity> findByCustomerId(Long customerId);

}
