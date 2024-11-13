package com.flik.repository;

import com.flik.entity.ZwitchBankDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZwitchBankDetailsRepository extends JpaRepository<ZwitchBankDetails , Long> {
}
