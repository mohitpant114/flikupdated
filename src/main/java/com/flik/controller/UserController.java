package com.flik.controller;

import com.flik.Service.TokenService;
import com.flik.Service.UserService;
import com.flik.entity.TokenEntity;
import com.flik.entity.UserEntity;
import com.flik.request.UserLoginRequest;
import com.flik.request.UserRequest;
import com.flik.respons.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/userCreated")

    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        try {
            userService.createUser(userRequest);
            return new ResponseEntity<>("User created successfully!", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Catching the specific exception for mobile number already exists
            if (e.getMessage().equals("Mobile number already exists")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }




//    @PostMapping("/login")
//    public ResponseEntity<String> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
//        String emailOrMobileNumber = userLoginRequest.getEmailOrMobileNumber();
//        String password = userLoginRequest.getPassword();
//
//        Optional<UserEntity> userOpt = emailOrMobileNumber.contains("@") ?
//                userService.findByEmail(emailOrMobileNumber) :
//                userService.findByMobileNumber(emailOrMobileNumber);
//
//        if (userOpt.isPresent() && userService.checkPassword(userOpt.get(), password)) {
//            return ResponseEntity.ok("Login successful");
//        } else {
//            return ResponseEntity.status(401).body("Invalid email/mobile number or password");
//        }
//    }

//    @PostMapping("/login")
//    public ResponseEntity<String> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
//        String emailOrMobileNumber = userLoginRequest.getEmailOrMobileNumber();
//        String password = userLoginRequest.getPassword();
//
//        // Validate user based on email or mobile number
//        Optional<UserEntity> userOpt = emailOrMobileNumber.contains("@") ?
//                userService.findByEmail(emailOrMobileNumber) :
//                userService.findByMobileNumber(emailOrMobileNumber);
//
//        if (userOpt.isPresent() && userService.checkPassword(userOpt.get(), password)) {
//            UserEntity user = userOpt.get();
//            String token = UUID.randomUUID().toString();
//
//            // Save the token in TokenEntity
//            TokenEntity tokenEntity = new TokenEntity();
//            tokenEntity.setUserId(user.getId());
//            tokenEntity.setMobileNumber(user.getSpocMobileNumber());
//            tokenEntity.setToken(token);
//            tokenEntity.setCreatedTime(LocalDateTime.now());
//
//            tokenService.saveToken(tokenEntity);
//
//            // Send the token to the frontend for future authentication
//            return ResponseEntity.ok(token);
//        } else {
//            return ResponseEntity.status(401).body("Invalid email/mobile number or password");
//        }
//    }
@PostMapping("/login")
public ResponseEntity<String> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
    String emailOrMobileNumber = userLoginRequest.getEmailOrMobileNumber();
    String password = userLoginRequest.getPassword();

    // Validate user based on email or mobile number
    Optional<UserEntity> userOpt = emailOrMobileNumber.contains("@") ?
            userService.findByEmail(emailOrMobileNumber) :
            userService.findByMobileNumber(emailOrMobileNumber);

    if (userOpt.isPresent() && userService.checkPassword(userOpt.get(), password)) {
        UserEntity user = userOpt.get();

        // Check if a token already exists for this user
        Optional<TokenEntity> tokenOpt = tokenService.findByUserIdAndMobileNumber(user.getId(), user.getSpocMobileNumber());

        TokenEntity tokenEntity;
        String newToken = UUID.randomUUID().toString();

        // If the token exists and is expired, update it
        if (tokenOpt.isPresent()) {
            tokenEntity = tokenOpt.get();

            if (LocalDateTime.now().isAfter(tokenEntity.getExpiryTime())) {
                // Token expired, update with a new token and expiry time
                tokenEntity.setToken(newToken);
                tokenEntity.setCreatedTime(LocalDateTime.now());
                tokenEntity.setExpiryTime(LocalDateTime.now().plusMinutes(60));
            }

        } else {
            // No token exists, create a new one
            tokenEntity = new TokenEntity();
            tokenEntity.setUserId(user.getId());
            tokenEntity.setMobileNumber(user.getSpocMobileNumber());
            tokenEntity.setToken(newToken);
            tokenEntity.setCreatedTime(LocalDateTime.now());
            tokenEntity.setExpiryTime(LocalDateTime.now().plusMinutes(60));
        }

        // Save or update the token in the database
        tokenService.saveToken(tokenEntity);

        // Send the new token to the frontend for future authentication
        return ResponseEntity.ok(newToken);
    } else {
        return ResponseEntity.status(401).body("Invalid email/mobile number or password");
    }
}


    @DeleteMapping("userDeleteById")
    public ResponseEntity<HttpStatus>  deleteUserById(@RequestParam("id") Long id  ) {
        try {
            userService.deleteUserById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("updateUser")
    public ResponseEntity<String> updateTutorial(@RequestParam("id") Long id, @RequestBody UserRequest userRequest) {
        try {
            userService.updateUserDetails(id, userRequest);
            return ResponseEntity.ok(" User Details updated successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/fintechList")
    public List<UserEntity> getAllFintechList() {

        return  userService.getAllFintechList();
    }


    @GetMapping("/userListById")
    public UserRequest getUserDetailsById(@RequestParam("id") Long userId) {
        return userService.getUserDetailsById(userId);
    }
    @GetMapping("/userToExport")
    public ResponseEntity<?> exportCustomerToExcel() {
        List<UserEntity> userEntities = userService.getAllFintechList();
        String fileName = "FlikUserExcel.xlsx";

        try {
            if (userEntities != null && !userEntities.isEmpty()) {
                byte[] excelContent = userService.exportAllUserDetailsToExcel(userEntities);
                ByteArrayResource resource = new ByteArrayResource(excelContent);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .contentLength(resource.contentLength())
                        .body(resource);

            } else {
                return new ResponseEntity<>("No User found to export", HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to export user to Excel", HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }




    @GetMapping("/searchByPartnerName")
    public List<UserEntity> searchByPartnerName(@RequestParam("partnerName") String fintechName) {
        return userService.searchByPartnerName(fintechName);
    }

    @GetMapping("/searchByGst")
    public List<UserEntity> searchByGst(@RequestParam("gst") String gstNumber) {
        return userService.searchByGst(gstNumber);
    }


    @GetMapping("/countFintechPartner")
    public long countTotalFintechPartners() {
        return userService.countTotalFintechPartners();
    }




//    @GetMapping
//    public List<UserDTO> getUsers() {
//        return userService.getAllUsers();
//    }
@GetMapping("userProfile")
public ResponseEntity<UserDTO> getUserByFintechId(@RequestParam("fintechId") String fintechId) {
    return userService.getUserByFintechId(fintechId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}


}
