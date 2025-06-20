package com.trainmanagement.controller;

import com.trainmanagement.model.dto.ApiResponse;
import com.trainmanagement.model.dto.PassengerDto;
import com.trainmanagement.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    // Get All Passengers - GET /api/passengers
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getAllPassengers() {
        try {
            Object passengers = passengerService.getAllPassengers();
            return ResponseEntity.ok(ApiResponse.success("All passengers retrieved successfully", passengers));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve passengers: " + e.getMessage(), 500, null));
        }
    }

    // Create New Passenger - POST /api/passengers
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createPassenger(@Valid @RequestBody PassengerDto passengerDto) {
        try {
            Object passenger = passengerService.createPassenger(passengerDto);
            return ResponseEntity.status(201)
                    .body(ApiResponse.success("Passenger '" + passengerDto.getName() + "' created successfully", passenger));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to create passenger: " + e.getMessage(), 500, null));
        }
    }

    // Get Passenger by ID - GET /api/passengers/{passengerId}
    @GetMapping("/{passengerId}")
    public ResponseEntity<ApiResponse<Object>> getPassengerById(@PathVariable Long passengerId) {
        try {
            Object passenger = passengerService.getPassengerById(passengerId);
            return ResponseEntity.ok(ApiResponse.success("Passenger with ID " + passengerId + " retrieved successfully", passenger));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve passenger: " + e.getMessage(), 500, null));
        }
    }

    // Update Passenger - PUT /api/passengers/{passengerId}
    @PutMapping("/{passengerId}")
    public ResponseEntity<ApiResponse<Object>> updatePassenger(
            @PathVariable Long passengerId,
            @Valid @RequestBody PassengerDto passengerDto) {
        try {
            Object passenger = passengerService.updatePassenger(passengerId, passengerDto);
            return ResponseEntity.ok(ApiResponse.success("Passenger with ID " + passengerId + " updated successfully", passenger));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to update passenger: " + e.getMessage(), 500, null));
        }
    }

    // Delete Passenger - DELETE /api/passengers/{passengerId}
    @DeleteMapping("/{passengerId}")
    public ResponseEntity<ApiResponse<String>> deletePassenger(@PathVariable Long passengerId) {
        try {
            String result = passengerService.deletePassenger(passengerId);
            return ResponseEntity.ok(ApiResponse.success("Passenger with ID " + passengerId + " deleted successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to delete passenger: " + e.getMessage(), 500, null));
        }
    }

    // Get Passengers by User - GET /api/passengers/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Object>> getPassengersByUser(@PathVariable Long userId) {
        try {
            Object passengers = passengerService.getPassengersByUser(userId);
            return ResponseEntity.ok(ApiResponse.success("All passengers for user ID " + userId + " retrieved successfully", passengers));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve user passengers: " + e.getMessage(), 500, null));
        }
    }
} 