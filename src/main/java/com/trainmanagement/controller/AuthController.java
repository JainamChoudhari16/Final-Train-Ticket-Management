package com.trainmanagement.controller;

import com.trainmanagement.model.dto.ApiResponse;
import com.trainmanagement.model.dto.LoginDto;
import com.trainmanagement.model.dto.UserRegistrationDto;
import com.trainmanagement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // User Registration - POST /api/register
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            String result = authService.registerUser(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User '" + registrationDto.getName() + "' with email " + 
                            registrationDto.getEmail() + " registered successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Registration failed: " + e.getMessage(), 500, null));
        }
    }

    // Admin Registration - POST /api/admin/register
    @PostMapping("/admin/register")
    public ResponseEntity<ApiResponse<String>> registerAdmin(@Valid @RequestBody LoginDto adminDto) {
        try {
            String result = authService.registerAdmin(adminDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Admin with email " + adminDto.getEmail() + " registered successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Admin registration failed: " + e.getMessage(), 500, null));
        }
    }

    // User Authentication - POST /api/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            String result = authService.authenticateUser(loginDto);
            return ResponseEntity.ok(ApiResponse.success("Login successful for user " + loginDto.getEmail(), result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Login failed: " + e.getMessage(), 401, null));
        }
    }

    // Account Activation - PUT /api/activate/{user_id}
    @PutMapping("/activate/{userId}")
    public ResponseEntity<ApiResponse<String>> activateAccount(@PathVariable Long userId) {
        try {
            String result = authService.activateAccount(userId);
            return ResponseEntity.ok(ApiResponse.success("Account for user ID " + userId + " activated successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Activation failed: " + e.getMessage(), 400, null));
        }
    }
} 