package com.flik.repository;

import com.flik.entity.AccountDetailsEntity;
import com.flik.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerAccountDetailsRepository extends JpaRepository<AccountDetailsEntity, Long> {


    Optional<AccountDetailsEntity> findByCustomerId(Long customerId);

}
