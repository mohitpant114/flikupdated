package com.flik.Service;

import com.flik.entity.AdminEntity;
import com.flik.repository.AdminRepository;
import com.flik.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;



    public boolean authenticate(LoginRequest loginRequest) {
        AdminEntity admin = adminRepository.findByAdminIdAndAdminPassword(
                loginRequest.getAdminId(), loginRequest.getAdminPassword());

        return admin != null;
    }


}
