package com.trainmanagement.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.trainmanagement.model.Train;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TrainResponseDto {
    private Long id;
    private String trainNumber;
    private String trainName;
    private String sourceStation;
    private String destinationStation;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime departureTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime arrivalTime;
    
    private Integer totalSeats;
    private Integer availableSeats;
    private Double price;
    private String status;
    private List<BookingResponseDto> bookings;

    public TrainResponseDto() {}

    public static TrainResponseDto fromTrain(Train train) {
        TrainResponseDto dto = new TrainResponseDto();
        dto.setId(train.getId());
        dto.setTrainNumber(train.getTrainNumber());
        dto.setTrainName(train.getTrainName());
        dto.setSourceStation(train.getSourceStation());
        dto.setDestinationStation(train.getDestinationStation());
        dto.setDepartureTime(train.getDepartureTime());
        dto.setArrivalTime(train.getArrivalTime());
        dto.setTotalSeats(train.getTotalSeats());
        dto.setAvailableSeats(train.getAvailableSeats());
        dto.setPrice(train.getPrice());
        dto.setStatus(train.getStatus() != null ? train.getStatus().toString() : null);
        
        if (train.getBookings() != null) {
            dto.setBookings(train.getBookings().stream()
                .map(BookingResponseDto::fromBooking)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public void setSourceStation(String sourceStation) {
        this.sourceStation = sourceStation;
    }

    public String getDestinationStation() {
        return destinationStation;
    }

    public void setDestinationStation(String destinationStation) {
        this.destinationStation = destinationStation;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BookingResponseDto> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingResponseDto> bookings) {
        this.bookings = bookings;
    }
} 