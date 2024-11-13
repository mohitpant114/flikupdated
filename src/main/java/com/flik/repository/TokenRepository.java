package com.flik.repository;

import com.flik.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByMobileNumber(String mobileNumber);
    Optional<TokenEntity> findByToken(String token);
    Optional<TokenEntity> findByUserIdAndMobileNumber(Long userId, String mobileNumber);

}
