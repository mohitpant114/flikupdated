package com.flik.repository;

import com.flik.entity.OverDueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OverDueRepository extends JpaRepository<OverDueEntity , Long> {

  //  OverDueEntity findByRepaymentId(Long repaymentId);
    Optional<OverDueEntity> findByRepaymentId(Long repaymentId);
  //  void deleteByRepaymentId(Long repaymentId);
}
