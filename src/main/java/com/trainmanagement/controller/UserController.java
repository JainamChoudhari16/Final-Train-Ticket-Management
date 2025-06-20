package com.trainmanagement.controller;

import com.trainmanagement.model.dto.ApiResponse;
import com.trainmanagement.model.dto.UserDto;
import com.trainmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Get User Profile - GET /api/user/profile
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Object>> getUserProfile(@RequestParam String email) {
        try {
            Object profile = userService.getUserProfile(email);
            return ResponseEntity.ok(ApiResponse.success("User profile for " + email + " retrieved successfully", profile));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve profile: " + e.getMessage(), 500, null));
        }
    }

    // Get User by ID - GET /api/user/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<Object>> getUserById(@PathVariable Long userId) {
        try {
            Object user = userService.getUserById(userId);
            return ResponseEntity.ok(ApiResponse.success("User with ID " + userId + " retrieved successfully", user));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve user: " + e.getMessage(), 500, null));
        }
    }

    // Get All Users - GET /api/user
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getAllUsers() {
        try {
            Object users = userService.getAllUsers();
            return ResponseEntity.ok(ApiResponse.success("All users retrieved successfully", users));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve users: " + e.getMessage(), 500, null));
        }
    }

    // Create User - POST /api/user
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createUser(@Valid @RequestBody UserDto userDto) {
        try {
            Object user = userService.createUser(userDto);
            return ResponseEntity.status(201)
                    .body(ApiResponse.success("User '" + userDto.getName() + "' with email " + 
                            userDto.getEmail() + " created successfully", user));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to create user: " + e.getMessage(), 500, null));
        }
    }

    // Update User Profile - PUT /api/user/profile
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<String>> updateUserProfile(@RequestParam String email, 
                                                                @Valid @RequestBody UserDto userDto) {
        try {
            String result = userService.updateUserProfile(email, userDto);
            return ResponseEntity.ok(ApiResponse.success("Profile for user " + email + " updated successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body(ApiResponse.error("Failed to update profile: " + e.getMessage(), 400, null));
        }
    }

    // Update User - PUT /api/user/{userId}
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<Object>> updateUser(@PathVariable Long userId, 
                                                         @Valid @RequestBody UserDto userDto) {
        try {
            Object user = userService.updateUser(userId, userDto);
            return ResponseEntity.ok(ApiResponse.success("User with ID " + userId + " updated successfully", user));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body(ApiResponse.error("Failed to update user: " + e.getMessage(), 400, null));
        }
    }

    // Delete User - DELETE /api/user/{userId}
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long userId) {
        try {
            String result = userService.deleteUser(userId);
            return ResponseEntity.ok(ApiResponse.success("User with ID " + userId + " deleted successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body(ApiResponse.error("Failed to delete user: " + e.getMessage(), 400, null));
        }
    }

    // Get Booking History - GET /api/user/history
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<Object>> getBookingHistory(@RequestParam String email) {
        try {
            Object history = userService.getBookingHistory(email);
            return ResponseEntity.ok(ApiResponse.success("Booking history for " + email + " retrieved successfully", history));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve booking history: " + e.getMessage(), 500, null));
        }
    }

    // Get Journey Information - GET /api/user/journey
    @GetMapping("/journey")
    public ResponseEntity<ApiResponse<Object>> getJourneyInfo(@RequestParam String email) {
        try {
            Object journeyInfo = userService.getJourneyInfo(email);
            return ResponseEntity.ok(ApiResponse.success("Journey information for " + email + " retrieved successfully", journeyInfo));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve journey information: " + e.getMessage(), 500, null));
        }
    }

    // Get Upcoming Trips - GET /api/user/upcoming
    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<Object>> getUpcomingTrips(@RequestParam String email) {
        try {
            Object upcomingTrips = userService.getUpcomingTrips(email);
            return ResponseEntity.ok(ApiResponse.success("Upcoming trips for " + email + " retrieved successfully", upcomingTrips));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve upcoming trips: " + e.getMessage(), 500, null));
        }
    }
} 