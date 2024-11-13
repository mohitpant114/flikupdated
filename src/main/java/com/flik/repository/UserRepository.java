package com.flik.repository;

import com.flik.entity.CustomerEntity;
import com.flik.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity , Long> {
    //UserEntity findByMobileNo(String mobileNo);
    Optional<UserEntity> findByEmailId(String email);
    Optional<UserEntity> findBySpocMobileNumber(String mobileNumber);

    List<UserEntity> findByFintechName(String fintechName);
    List<UserEntity> findByGstNumber(String gstNumber);


    //UserEntity findByFintechId(String fintechId);
    Optional<UserEntity> findByFintechId(String fintechId);

}
