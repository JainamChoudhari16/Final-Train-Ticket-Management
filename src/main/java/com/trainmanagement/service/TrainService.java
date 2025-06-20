package com.trainmanagement.service;

import com.trainmanagement.model.Train;
import com.trainmanagement.model.Booking;
import com.trainmanagement.model.Ticket;
import com.trainmanagement.model.dto.TrainDto;
import com.trainmanagement.repository.TrainRepository;
import com.trainmanagement.repository.BookingRepository;
import com.trainmanagement.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.ArrayList;

@Service
public class TrainService {

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    public Train createTrain(TrainDto trainDto) {
        // Parse dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime departureTime = LocalDateTime.parse(trainDto.getDepartureTime(), formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(trainDto.getArrivalTime(), formatter);
        
        // Check if train number already exists
        List<Train> existingTrains = trainRepository.findBySourceStationAndDestinationStation(
                trainDto.getSourceStation(), trainDto.getDestinationStation());
        boolean trainExists = existingTrains.stream()
                .anyMatch(train -> train.getTrainNumber().equals(trainDto.getTrainNumber()));
        
        if (trainExists) {
            throw new RuntimeException("Train with this number already exists");
        }
        
        // Create new train
        Train newTrain = new Train(trainDto.getTrainNumber(), trainDto.getTrainName(), 
                trainDto.getSourceStation(), trainDto.getDestinationStation(),
                departureTime, arrivalTime, trainDto.getTotalSeats(), trainDto.getPrice());
        Train savedTrain = trainRepository.save(newTrain);
        
        return savedTrain;
    }

    public List<Train> searchTrains(String sourceStation, String destinationStation, String date) {
        if (date != null && !date.isEmpty()) {
            try {
                LocalDateTime searchDate = LocalDateTime.parse(date + "T00:00:00");
                LocalDateTime endDate = searchDate.plusDays(1);
                
                return trainRepository.findBySourceStationAndDestinationStationAndDepartureTimeBetween(
                        sourceStation, destinationStation, searchDate, endDate);
            } catch (Exception e) {
                throw new RuntimeException("Invalid date format. Use yyyy-MM-dd");
            }
        } else {
            return trainRepository.findBySourceStationAndDestinationStation(sourceStation, destinationStation);
        }
    }

    public Train getTrainById(Long trainId) {
        Optional<Train> trainOpt = trainRepository.findById(trainId);
        
        if (trainOpt.isEmpty()) {
            throw new RuntimeException("Train not found");
        }
        
        return trainOpt.get();
    }

    public Train updateTrain(Long trainId, TrainDto trainDto) {
        Optional<Train> trainOpt = trainRepository.findById(trainId);
        
        if (trainOpt.isEmpty()) {
            throw new RuntimeException("Train not found");
        }
        
        Train train = trainOpt.get();
        
        // Update fields
        train.setTrainNumber(trainDto.getTrainNumber());
        train.setTrainName(trainDto.getTrainName());
        train.setSourceStation(trainDto.getSourceStation());
        train.setDestinationStation(trainDto.getDestinationStation());
        
        // Parse and update times
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime departureTime = LocalDateTime.parse(trainDto.getDepartureTime(), formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(trainDto.getArrivalTime(), formatter);
        train.setDepartureTime(departureTime);
        train.setArrivalTime(arrivalTime);
        
        // Update seats and price
        train.setTotalSeats(trainDto.getTotalSeats());
        train.setAvailableSeats(trainDto.getTotalSeats());
        train.setPrice(trainDto.getPrice());
        
        Train updatedTrain = trainRepository.save(train);
        return updatedTrain;
    }

    @Transactional
    public String deleteTrain(Long trainId) {
        Optional<Train> trainOpt = trainRepository.findById(trainId);
        
        if (trainOpt.isEmpty()) {
            throw new RuntimeException("Train not found");
        }
        
        Train train = trainOpt.get();
        
        // First, delete all tickets associated with this train's bookings
        List<Booking> relatedBookings = bookingRepository.findByTrain(train);
        for (Booking booking : relatedBookings) {
            // Delete related tickets
            ticketRepository.deleteByBooking(booking);
        }
        
        // Then delete all bookings
        bookingRepository.deleteByTrain(train);
        
        // Finally delete the train
        trainRepository.delete(train);
        
        return "Train and all related records deleted successfully";
    }

    public Map<String, Object> getAvailableSeats(Long trainId, String journeyDate) {
        Optional<Train> trainOpt = trainRepository.findById(trainId);
        
        if (trainOpt.isEmpty()) {
            throw new RuntimeException("Train not found");
        }
        
        Train train = trainOpt.get();
        
        try {
            // Parse the date string using DateTimeFormatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime journeyDateTime = LocalDate.parse(journeyDate, formatter).atStartOfDay();
            LocalDateTime journeyEndDateTime = journeyDateTime.plusDays(1);
            
            if (journeyDateTime.isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Journey date cannot be in the past");
            }

            // Get confirmed bookings for this train on the specified date
            List<Booking> existingBookings = bookingRepository.findByTrainIdAndJourneyDate(trainId, journeyDateTime);
            int bookedSeats = (int) existingBookings.stream()
                .filter(booking -> booking.getStatus() == Booking.BookingStatus.CONFIRMED)
                .count();

            // Calculate available seats
            int availableSeats = train.getTotalSeats() - bookedSeats;
            
            // Create seat availability response
            Map<String, Object> seatInfo = new HashMap<>();
            seatInfo.put("trainId", train.getId());
            seatInfo.put("trainNumber", train.getTrainNumber());
            seatInfo.put("trainName", train.getTrainName());
            seatInfo.put("totalSeats", train.getTotalSeats());
            seatInfo.put("availableSeats", availableSeats);
            seatInfo.put("journeyDate", journeyDate);
            
            // Generate available seat numbers
            List<String> availableSeatNumbers = new ArrayList<>();
            for (int i = 1; i <= availableSeats; i++) {
                availableSeatNumbers.add("Seat-" + i);
            }
            seatInfo.put("availableSeatNumbers", availableSeatNumbers);
            
            return seatInfo;
            
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format. Use yyyy-MM-dd");
        }
    }

    public List<Train> getTrainSchedule(String sourceStation, String destinationStation) {
        if (sourceStation != null && destinationStation != null) {
            return trainRepository.findBySourceStationAndDestinationStation(sourceStation, destinationStation);
        } else {
            return trainRepository.findAvailableTrains();
        }
    }
} 