package com.trainmanagement.controller;

import com.trainmanagement.model.dto.ApiResponse;
import com.trainmanagement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Upload Train Schedules - POST /api/admin/active_trains/upload
    @PostMapping("/active_trains/upload")
    public ResponseEntity<ApiResponse<String>> uploadTrainSchedules(@RequestParam("file") MultipartFile file) {
        try {
            String result = adminService.uploadTrainSchedules(file);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Train schedules uploaded successfully from file: " + file.getOriginalFilename(), result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Upload failed: " + e.getMessage(), 500, null));
        }
    }

    // Get System Statistics - GET /api/admin/statistics
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Object>> getSystemStatistics() {
        try {
            Object statistics = adminService.getSystemStatistics();
            return ResponseEntity.ok(ApiResponse.success("System statistics retrieved successfully", statistics));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve statistics: " + e.getMessage(), 500, null));
        }
    }

    // Get All Users - GET /api/admin/users
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Object>> getAllUsers() {
        try {
            Object users = adminService.getAllUsers();
            return ResponseEntity.ok(ApiResponse.success("All users retrieved successfully for admin", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve users: " + e.getMessage(), 500, null));
        }
    }

    // Update User Status - PUT /api/admin/users/{userId}/status
    @PutMapping("/users/{userId}/status")
    public ResponseEntity<ApiResponse<String>> updateUserStatus(@PathVariable Long userId, 
                                                               @RequestParam String status) {
        try {
            String result = adminService.updateUserStatus(userId, status);
            return ResponseEntity.ok(ApiResponse.success("User status for user ID " + userId + " updated to '" + status + "' successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update user status: " + e.getMessage(), 400, null));
        }
    }

    // Get All Trains - GET /api/admin/trains
    @GetMapping("/trains")
    public ResponseEntity<ApiResponse<Object>> getAllTrains() {
        try {
            Object trains = adminService.getAllTrains();
            return ResponseEntity.ok(ApiResponse.success("All trains retrieved successfully for admin", trains));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve trains: " + e.getMessage(), 500, null));
        }
    }

    // Update Train Status - PUT /api/admin/trains/{trainId}/status
    @PutMapping("/trains/{trainId}/status")
    public ResponseEntity<ApiResponse<String>> updateTrainStatus(@PathVariable Long trainId, 
                                                                @RequestParam String status) {
        try {
            String result = adminService.updateTrainStatus(trainId, status);
            return ResponseEntity.ok(ApiResponse.success("Train status for train ID " + trainId + " updated to '" + status + "' successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update train status: " + e.getMessage(), 400, null));
        }
    }
} 