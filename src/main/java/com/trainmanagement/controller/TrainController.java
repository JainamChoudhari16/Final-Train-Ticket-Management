package com.trainmanagement.controller;

import com.trainmanagement.model.Train;
import com.trainmanagement.model.dto.ApiResponse;
import com.trainmanagement.model.dto.TrainDto;
import com.trainmanagement.model.dto.TrainResponseDto;
import com.trainmanagement.service.TrainService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trains")
public class TrainController {

    @Autowired
    private TrainService trainService;

    // Get All Trains - GET /api/trains
    @GetMapping
    public ResponseEntity<ApiResponse<List<TrainResponseDto>>> getAllTrains() {
        try {
            List<Train> trains = trainService.getAllTrains();
            List<TrainResponseDto> trainDtos = trains.stream()
                .map(TrainResponseDto::fromTrain)
                .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("All trains retrieved successfully", trainDtos));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve trains: " + e.getMessage(), 500, null));
        }
    }

    // Create New Train - POST /api/trains
    @PostMapping
    public ResponseEntity<ApiResponse<TrainResponseDto>> createTrain(@Valid @RequestBody TrainDto trainDto) {
        try {
            Train train = trainService.createTrain(trainDto);
            TrainResponseDto responseDto = TrainResponseDto.fromTrain(train);
            return ResponseEntity.status(201)
                    .body(ApiResponse.success("Train '" + trainDto.getTrainName() + "' created successfully", responseDto));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to create train: " + e.getMessage(), 500, null));
        }
    }

    // Search Trains by Route - GET /api/trains/search
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TrainResponseDto>>> searchTrains(
            @RequestParam String sourceStation,
            @RequestParam String destinationStation,
            @RequestParam(required = false) String date) {
        try {
            List<Train> trains = trainService.searchTrains(sourceStation, destinationStation, date);
            List<TrainResponseDto> trainDtos = trains.stream()
                .map(TrainResponseDto::fromTrain)
                .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Trains found successfully", trainDtos));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to search trains: " + e.getMessage(), 500, null));
        }
    }

    // Get Train by ID - GET /api/trains/{trainId}
    @GetMapping("/{trainId}")
    public ResponseEntity<ApiResponse<TrainResponseDto>> getTrainById(@PathVariable Long trainId) {
        try {
            Train train = trainService.getTrainById(trainId);
            TrainResponseDto responseDto = TrainResponseDto.fromTrain(train);
            return ResponseEntity.ok(ApiResponse.success("Train retrieved successfully", responseDto));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve train: " + e.getMessage(), 500, null));
        }
    }

    // Update Train - PUT /api/trains/{trainId}
    @PutMapping("/{trainId}")
    public ResponseEntity<ApiResponse<TrainResponseDto>> updateTrain(
            @PathVariable Long trainId,
            @Valid @RequestBody TrainDto trainDto) {
        try {
            Train train = trainService.updateTrain(trainId, trainDto);
            TrainResponseDto responseDto = TrainResponseDto.fromTrain(train);
            return ResponseEntity.ok(ApiResponse.success("Train updated successfully", responseDto));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to update train: " + e.getMessage(), 500, null));
        }
    }

    // Delete Train - DELETE /api/trains/{trainId}
    @DeleteMapping("/{trainId}")
    public ResponseEntity<ApiResponse<String>> deleteTrain(@PathVariable Long trainId) {
        try {
            String result = trainService.deleteTrain(trainId);
            return ResponseEntity.ok(ApiResponse.success("Train deleted successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to delete train: " + e.getMessage(), 500, null));
        }
    }

    // Get Available Seats - GET /api/trains/{trainId}/seats
    @GetMapping("/{trainId}/seats")
    public ResponseEntity<ApiResponse<Object>> getAvailableSeats(
            @PathVariable Long trainId,
            @RequestParam String journeyDate) {
        try {
            Object seats = trainService.getAvailableSeats(trainId, journeyDate);
            return ResponseEntity.ok(ApiResponse.success("Available seats retrieved successfully", seats));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve available seats: " + e.getMessage(), 500, null));
        }
    }

    // Get Train Schedule - GET /api/trains/schedule
    @GetMapping("/schedule")
    public ResponseEntity<ApiResponse<List<TrainResponseDto>>> getTrainSchedule(
            @RequestParam(required = false) String sourceStation,
            @RequestParam(required = false) String destinationStation) {
        try {
            List<Train> trains = trainService.getTrainSchedule(sourceStation, destinationStation);
            List<TrainResponseDto> trainDtos = trains.stream()
                .map(TrainResponseDto::fromTrain)
                .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Train schedule retrieved successfully", trainDtos));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve train schedule: " + e.getMessage(), 500, null));
        }
    }
} 