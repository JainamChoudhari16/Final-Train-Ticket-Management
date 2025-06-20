package com.trainmanagement.controller;

import com.trainmanagement.model.dto.ApiResponse;
import com.trainmanagement.model.dto.BookingDto;
import com.trainmanagement.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Get All Bookings - GET /api/bookings
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getAllBookings() {
        try {
            Object bookings = bookingService.getAllBookings();
            return ResponseEntity.ok(ApiResponse.success("All bookings retrieved successfully", bookings));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve bookings: " + e.getMessage(), 500, null));
        }
    }

    // Create New Booking - POST /api/bookings
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createBooking(@Valid @RequestBody BookingDto bookingDto) {
        try {
            Object booking = bookingService.createBooking(bookingDto);
            return ResponseEntity.status(201)
                    .body(ApiResponse.success("Booking created successfully for train ID " + 
                            bookingDto.getTrainId() + " with seat " + bookingDto.getSeatNumber(), booking));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to create booking: " + e.getMessage(), 500, null));
        }
    }

    // Get Booking by ID - GET /api/bookings/{bookingId}
    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<Object>> getBookingById(@PathVariable Long bookingId) {
        try {
            Object booking = bookingService.getBookingById(bookingId);
            return ResponseEntity.ok(ApiResponse.success("Booking with ID " + bookingId + " retrieved successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve booking: " + e.getMessage(), 500, null));
        }
    }

    // Update Booking - PUT /api/bookings/{bookingId}
    @PutMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<Object>> updateBooking(
            @PathVariable Long bookingId,
            @Valid @RequestBody BookingDto bookingDto) {
        try {
            Object booking = bookingService.updateBooking(bookingId, bookingDto);
            return ResponseEntity.ok(ApiResponse.success("Booking with ID " + bookingId + " updated successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to update booking: " + e.getMessage(), 500, null));
        }
    }

    // Delete Booking - DELETE /api/bookings/{bookingId}
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<String>> deleteBooking(@PathVariable Long bookingId) {
        try {
            String result = bookingService.deleteBooking(bookingId);
            return ResponseEntity.ok(ApiResponse.success("Booking with ID " + bookingId + " deleted successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to delete booking: " + e.getMessage(), 500, null));
        }
    }

    // Get Bookings by User - GET /api/bookings/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Object>> getBookingsByUser(@PathVariable Long userId) {
        try {
            Object bookings = bookingService.getBookingsByUser(userId);
            return ResponseEntity.ok(ApiResponse.success("All bookings for user ID " + userId + " retrieved successfully", bookings));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve user bookings: " + e.getMessage(), 500, null));
        }
    }

    // Cancel Booking - PUT /api/bookings/{bookingId}/cancel
    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<ApiResponse<Object>> cancelBooking(@PathVariable Long bookingId) {
        try {
            Object booking = bookingService.cancelBooking(bookingId);
            return ResponseEntity.ok(ApiResponse.success("Booking with ID " + bookingId + " cancelled successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to cancel booking: " + e.getMessage(), 500, null));
        }
    }
} 