package com.visitinglecturer.vlms_backend.controller;

import com.visitinglecturer.vlms_backend.entity.User;
import com.visitinglecturer.vlms_backend.payload.request.LoginRequest;
import com.visitinglecturer.vlms_backend.payload.response.LoginResponse;
import com.visitinglecturer.vlms_backend.repository.UserRepository;
import com.visitinglecturer.vlms_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Ensure this is correct
@CrossOrigin(origins = "http://localhost:5173") // CORS configuration for React
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login") // Ensure this is the correct POST endpoint
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = authService.login(loginRequest);
            if (loginResponse != null) {
                return ResponseEntity.ok(loginResponse);
            }else {
                return ResponseEntity.status(401).body("Unauthorized");
            }
        }catch (Exception e) {
            return ResponseEntity.status(500).body("Error in login : " + e.getMessage() + e.getCause());
        }
    }

    @GetMapping("/create-hard-coded-users")
    public String createHardCordedUsers(){
        return authService.createHardCordedUsers();
    }


}