package com.flik.Service;

import com.flik.entity.UserEntity;
import com.flik.repository.UserRepository;
import com.flik.request.UserRequest;
import com.flik.respons.UserDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

        @Autowired
        private UserRepository userRepository;



        public void  createUser(UserRequest userRequest) throws Exception {
            Optional<UserEntity> existingUser = userRepository.findBySpocMobileNumber(userRequest.getSpocMobileNumber());
            if (existingUser.isPresent())
            {
                throw new RuntimeException("Mobile number already exists");
            }

            UserEntity user= new UserEntity();

          user.setFintechId(userRequest.getFintechId());
          user.setFintechName(userRequest.getFintechName());
          user.setPartnerAccountNumber(userRequest.getPartnerAccountNumber());
          user.setIfscCode(userRequest.getIfscCode());
          user.setBankName(userRequest.getBankName());
          user.setProcessingFeePart(userRequest.getProcessingFeePart());
          user.setCommision(userRequest.getCommision());
          user.setPan(userRequest.getPan());
          user.setCinNumber(userRequest.getCinNumber());
          user.setGstNumber(userRequest.getGstNumber());
          user.setDirectorName(userRequest.getDirectorName());
          user.setDirectorPan(userRequest.getDirectorPan());
          user.setDirectorAadhar(userRequest.getDirectorAadhar());
          user.setSpocName(userRequest.getSpocName());
          user.setSpocMobileNumber(userRequest.getSpocMobileNumber());
          user.setEmailId(userRequest.getEmailId());
//          user.setCreatePassword(userRequest.getCreatePassword());
//          user.setConfirmPassword(userRequest.getConfirmPassword());

            String createPassword = userRequest.getCreatePassword();
            String confirmPassword = userRequest.getConfirmPassword();

            // Set password
            user.setCreatePassword(createPassword);
            user.setConfirmPassword(confirmPassword);

            if (!createPassword.equals(confirmPassword)) {
                throw new Exception("Passwords do not match!");
            }

            //  length
            if (createPassword.length() < 8 || createPassword.length() > 16) {
                throw new Exception("Password must be between 8 and 16 characters long.");
            }

            // atLeast one uppercase letter
            if (!createPassword.matches(".*[A-Z].*")) {
                throw new Exception("Password must contain at least one uppercase letter.");
            }

            // atLeast one lowercase letter
            if (!createPassword.matches(".*[a-z].*")) {
                throw new Exception("Password must contain at least one lowercase letter.");
            }

            //  atLeast one digit
            if (!createPassword.matches(".*\\d.*")) {
                throw new Exception("Password must contain at least one digit.");
            }
            //  atLeast one special character
            if (!createPassword.matches(".*[!@#$%^&*()].*")) {
                throw new Exception("Password must contain at least one special character (@, #, $, etc.).");
            }
            //System.out.println("Password accepted!");


            user.setStatus(userRequest.getStatus());

          userRepository.save(user);
        }




    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmailId(email);
    }

    public Optional<UserEntity> findByMobileNumber(String mobileNumber) {
        return userRepository.findBySpocMobileNumber(mobileNumber);
    }
    public boolean checkPassword(UserEntity user, String rawPassword) {
        return user.getCreatePassword().equals(rawPassword);
    }

//    public boolean checkPassword(UserEntity user, String rawPassword) {
//        return bCryptPasswordEncoder.matches(rawPassword, user.getCreatePassword());
//    }




    public void deleteUserById(Long id)
    {
        userRepository.deleteById(id);
    }


    public void updateUserDetails(Long id, UserRequest userRequest) throws Exception{
        Optional< UserEntity> user = userRepository.findById(id);

        if (user.isPresent()) {
            UserEntity user1 = user.get();
             user1.setFintechId(userRequest.getFintechId());
             user1.setFintechName(userRequest.getFintechName());
             user1.setPartnerAccountNumber(userRequest.getPartnerAccountNumber());
             user1.setIfscCode(userRequest.getIfscCode());
             user1.setBankName(userRequest.getBankName());
             user1.setProcessingFeePart(userRequest.getProcessingFeePart());
             user1.setCommision(userRequest.getCommision());
             user1.setPan(userRequest.getPan());
             user1.setCinNumber(userRequest.getCinNumber());
             user1.setGstNumber(userRequest.getGstNumber());
             user1.setDirectorAadhar(userRequest.getDirectorAadhar());
             user1.setDirectorName(userRequest.getDirectorName());
             user1.setDirectorPan(userRequest.getDirectorPan());
             user1.setSpocName(userRequest.getSpocName());
             user1.setSpocMobileNumber(userRequest.getSpocMobileNumber());
             user1.setEmailId(userRequest.getEmailId());
             user1.setCreatePassword(userRequest.getCreatePassword());
             user1.setConfirmPassword(userRequest.getConfirmPassword());
             user1.setStatus(userRequest.getStatus());

            String createPassword = userRequest.getCreatePassword();
            String confirmPassword = userRequest.getConfirmPassword();

            // Set password
            user1.setCreatePassword(createPassword);
            user1.setConfirmPassword(confirmPassword);

            if (!createPassword.equals(confirmPassword)) {
                throw new Exception("Passwords do not match!");
            }

            //  length
            if (createPassword.length() < 8 || createPassword.length() > 16) {
                throw new Exception("Password must be between 8 and 16 characters long.");
            }

            // atLeast one uppercase letter
            if (!createPassword.matches(".*[A-Z].*")) {
                throw new Exception("Password must contain at least one uppercase letter.");
            }

            // atLeast one lowercase letter
            if (!createPassword.matches(".*[a-z].*")) {
                throw new Exception("Password must contain at least one lowercase letter.");
            }

            //  atLeast one digit
            if (!createPassword.matches(".*\\d.*")) {
                throw new Exception("Password must contain at least one digit.");
            }
            //  atLeast one special character
            if (!createPassword.matches(".*[!@#$%^&*()].*")) {
                throw new Exception("Password must contain at least one special character (@, #, $, etc.).");
            }


            userRepository.save(user1);
        } else {
            throw new NullPointerException("User Details not found with id: " + id);
        }
    }

    public List<UserEntity> getAllFintechList() {

        return userRepository.findAll();
    }

    public UserRequest getUserDetailsById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        UserRequest userRequest = new UserRequest();
        userRequest.setId(user.getId());
        userRequest.setFintechId(user.getFintechId());
        userRequest.setFintechName(user.getFintechName());
        userRequest.setPartnerAccountNumber(user.getPartnerAccountNumber());
        userRequest.setIfscCode(user.getIfscCode());
        userRequest.setBankName(user.getBankName());
        userRequest.setProcessingFeePart(user.getProcessingFeePart());
        userRequest.setCommision(user.getCommision());
        userRequest.setPan(user.getPan());
        userRequest.setCinNumber(user.getCinNumber());
        userRequest.setGstNumber(user.getGstNumber());
        userRequest.setDirectorName(user.getDirectorName());
        userRequest.setDirectorPan(user.getDirectorPan());
        userRequest.setDirectorAadhar(user.getDirectorAadhar());
        userRequest.setSpocName(user.getSpocName());
        userRequest.setSpocMobileNumber(user.getSpocMobileNumber());
        userRequest.setEmailId(user.getEmailId());
        userRequest.setCreatePassword(user.getCreatePassword());
        userRequest.setConfirmPassword(user.getConfirmPassword());
        userRequest.setStatus(user.getStatus());

        return userRequest;

    }
    public byte[] exportAllUserDetailsToExcel(List<UserEntity> userDetails) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet("User Details");
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "ID", "Partner Id", "Partner Name", "Partner Account Number", "IFSC Code", "Bank Name", "Processing Fee Part",
                "Commission", "PAN", "CIN Number", "GST Number", "Director Name", "Director PAN", "Director Aadhar",
                "SPOC Name", "SPOC Mobile Number", "Email ID", "Create Password", "Confirm Password"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (UserEntity user : userDetails) {
            Row dataRow = sheet.createRow(rowNum++);

            dataRow.createCell(0).setCellValue(user.getId());
            dataRow.createCell(1).setCellValue(user.getFintechId());
            dataRow.createCell(2).setCellValue(user.getFintechName());
            dataRow.createCell(3).setCellValue(user.getPartnerAccountNumber());
            dataRow.createCell(4).setCellValue(user.getIfscCode());
            dataRow.createCell(5).setCellValue(user.getBankName());
            dataRow.createCell(6).setCellValue(user.getProcessingFeePart());
            dataRow.createCell(7).setCellValue(user.getCommision());
            dataRow.createCell(8).setCellValue(user.getPan());
            dataRow.createCell(9).setCellValue(user.getCinNumber());
            dataRow.createCell(10).setCellValue(user.getGstNumber());
            dataRow.createCell(11).setCellValue(user.getDirectorName());
            dataRow.createCell(12).setCellValue(user.getDirectorPan());
            dataRow.createCell(13).setCellValue(user.getDirectorAadhar());
            dataRow.createCell(14).setCellValue(user.getSpocName());
            dataRow.createCell(15).setCellValue(user.getSpocMobileNumber());
            dataRow.createCell(16).setCellValue(user.getEmailId());
            dataRow.createCell(17).setCellValue(user.getCreatePassword());
            dataRow.createCell(18).setCellValue(user.getConfirmPassword());
        }

        workbook.write(byteArrayOutputStream);
        workbook.close();
        return byteArrayOutputStream.toByteArray();
    }


    public List<UserEntity> searchByPartnerName(String partnerName) {
        return userRepository.findByFintechName(partnerName);
    }

    public List<UserEntity> searchByGst(String gstNumber) {
        return userRepository.findByGstNumber(gstNumber);
    }



    public long countTotalFintechPartners() {
        return userRepository.count();
    }

    public Optional<UserDTO> getUserByFintechId(String fintechId) {
        return userRepository.findByFintechId(fintechId).map(user -> {
            UserDTO dto = new UserDTO();
            dto.setFintechId(user.getFintechId());
            dto.setDirectorPan(user.getDirectorPan());
            dto.setSpocMobileNumber(user.getSpocMobileNumber());
            dto.setFintechName(user.getFintechName());
            dto.setDirectorName(user.getDirectorName());
            dto.setGstNumber(user.getGstNumber());
            return dto;
        });
    }

}

