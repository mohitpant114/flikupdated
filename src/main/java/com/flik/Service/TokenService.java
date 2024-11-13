package com.flik.Service;

import com.flik.entity.TokenEntity;
import com.flik.entity.UserEntity;
import com.flik.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {



        @Autowired
        private TokenRepository tokenRepository;

        public void saveToken(TokenEntity tokenEntity) {
            tokenRepository.save(tokenEntity);
        }

        public Optional<TokenEntity> findByToken(String token) {
            return tokenRepository.findByToken(token);
        }
    public Optional<TokenEntity> findByUserIdAndMobileNumber(Long userId, String mobileNumber) {
        // Fetch the token by user ID and mobile number
        return tokenRepository.findByUserIdAndMobileNumber(userId, mobileNumber);
    }
    }


