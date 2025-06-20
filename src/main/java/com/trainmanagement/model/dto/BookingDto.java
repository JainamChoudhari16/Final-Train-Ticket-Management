package com.trainmanagement.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public class BookingDto {
    
    @NotNull(message = "Train ID is required")
    @Positive(message = "Train ID must be positive")
    private Long trainId;
    
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;
    
    @NotBlank(message = "Passenger name is required")
    private String passengerName;
    
    @NotNull(message = "Passenger age is required")
    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 120, message = "Age must be at most 120")
    private Integer passengerAge;
    
    @NotBlank(message = "Passenger gender is required")
    private String passengerGender;
    
    @NotBlank(message = "Journey date is required")
    private String journeyDate;
    
    @NotBlank(message = "Seat number is required")
    private String seatNumber;
    
    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    private Double totalAmount;
    
    // Constructors
    public BookingDto() {}
    
    public BookingDto(Long trainId, Long userId, String passengerName, Integer passengerAge, String passengerGender,
                     String journeyDate, String seatNumber, Double totalAmount) {
        this.trainId = trainId;
        this.userId = userId;
        this.passengerName = passengerName;
        this.passengerAge = passengerAge;
        this.passengerGender = passengerGender;
        this.journeyDate = journeyDate;
        this.seatNumber = seatNumber;
        this.totalAmount = totalAmount;
    }
    
    // Getters and Setters
    public Long getTrainId() {
        return trainId;
    }
    
    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getPassengerName() {
        return passengerName;
    }
    
    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }
    
    public Integer getPassengerAge() {
        return passengerAge;
    }
    
    public void setPassengerAge(Integer passengerAge) {
        this.passengerAge = passengerAge;
    }
    
    public String getPassengerGender() {
        return passengerGender;
    }
    
    public void setPassengerGender(String passengerGender) {
        this.passengerGender = passengerGender;
    }
    
    public String getJourneyDate() {
        return journeyDate;
    }
    
    public void setJourneyDate(String journeyDate) {
        this.journeyDate = journeyDate;
    }
    
    public String getSeatNumber() {
        return seatNumber;
    }
    
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    
    public Double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
} 