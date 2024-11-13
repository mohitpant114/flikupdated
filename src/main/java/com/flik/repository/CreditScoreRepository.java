package com.flik.repository;

import com.flik.entity.CreditScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditScoreRepository extends JpaRepository<CreditScoreEntity, Long> {


   // CreditScoreEntity findByCustomerId(Long customerId);
    Optional<CreditScoreEntity> findByCustomerId(Long customerId);
//@Query("SELECT c FROM CreditScoreEntity c WHERE CAST(c.customerId AS string) = :customerId")
//CreditScoreEntity findByCustomerId(@Param("customerId") String customerId);
}
