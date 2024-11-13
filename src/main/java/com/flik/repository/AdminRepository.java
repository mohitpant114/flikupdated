package com.flik.repository;

import com.flik.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminEntity , Long> {


    AdminEntity  findByAdminIdAndAdminPassword (String adminId, String adminPassword);
    Optional<AdminEntity> findByAdminId(String adminId);



}
