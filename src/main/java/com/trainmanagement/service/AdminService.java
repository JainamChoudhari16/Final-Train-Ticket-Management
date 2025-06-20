package com.trainmanagement.service;

import com.trainmanagement.model.Login;
import com.trainmanagement.model.Train;
import com.trainmanagement.model.User;
import com.trainmanagement.repository.LoginRepository;
import com.trainmanagement.repository.TrainRepository;
import com.trainmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private TrainRepository trainRepository;

    public String uploadTrainSchedules(MultipartFile file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            int addedCount = 0;
            int updatedCount = 0;
            
            // Skip header line
            reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                
                if (data.length < 8) {
                    continue; // Skip invalid lines
                }
                
                String trainNumber = data[0].trim();
                String trainName = data[1].trim();
                String sourceStation = data[2].trim();
                String destinationStation = data[3].trim();
                String departureTimeStr = data[4].trim();
                String arrivalTimeStr = data[5].trim();
                Integer totalSeats = Integer.parseInt(data[6].trim());
                Double price = Double.parseDouble(data[7].trim());
                
                // Parse dates
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime departureTime = LocalDateTime.parse(departureTimeStr, formatter);
                LocalDateTime arrivalTime = LocalDateTime.parse(arrivalTimeStr, formatter);
                
                // Check if train already exists
                List<Train> existingTrains = trainRepository.findBySourceStationAndDestinationStation(sourceStation, destinationStation);
                boolean trainExists = existingTrains.stream()
                        .anyMatch(train -> train.getTrainNumber().equals(trainNumber));
                
                if (trainExists) {
                    // Update existing train
                    Train existingTrain = existingTrains.stream()
                            .filter(train -> train.getTrainNumber().equals(trainNumber))
                            .findFirst().orElse(null);
                    
                    if (existingTrain != null) {
                        existingTrain.setTrainName(trainName);
                        existingTrain.setDepartureTime(departureTime);
                        existingTrain.setArrivalTime(arrivalTime);
                        existingTrain.setTotalSeats(totalSeats);
                        existingTrain.setAvailableSeats(totalSeats);
                        existingTrain.setPrice(price);
                        existingTrain.setStatus(Train.TrainStatus.ACTIVE);
                        
                        trainRepository.save(existingTrain);
                        updatedCount++;
                    }
                } else {
                    // Create new train
                    Train newTrain = new Train(trainNumber, trainName, sourceStation, destinationStation,
                            departureTime, arrivalTime, totalSeats, price);
                    trainRepository.save(newTrain);
                    addedCount++;
                }
            }
            
            reader.close();
            return "Added " + addedCount + " new trains, Updated " + updatedCount + " existing trains";
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload train schedules: " + e.getMessage());
        }
    }

    public Object getSystemStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        long totalUsers = userRepository.count();
        long totalTrains = trainRepository.count();
        long activeTrains = trainRepository.findByStatus(Train.TrainStatus.ACTIVE).size();
        long totalAdmins = loginRepository.findByEmailAndUserType("admin@example.com", Login.UserType.ADMIN).isPresent() ? 1 : 0;
        
        statistics.put("totalUsers", totalUsers);
        statistics.put("totalTrains", totalTrains);
        statistics.put("activeTrains", activeTrains);
        statistics.put("totalAdmins", totalAdmins);
        
        return statistics;
    }

    public Object getAllUsers() {
        return userRepository.findAll();
    }

    public String updateUserStatus(Long userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Login login = loginRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Login record not found"));
        
        if ("ACTIVE".equalsIgnoreCase(status)) {
            login.setStatus(Login.Status.ACTIVE);
        } else if ("INACTIVE".equalsIgnoreCase(status)) {
            login.setStatus(Login.Status.INACTIVE);
        } else {
            throw new RuntimeException("Invalid status. Use ACTIVE or INACTIVE");
        }
        
        loginRepository.save(login);
        return "User status updated to " + status;
    }

    public Object getAllTrains() {
        return trainRepository.findAll();
    }

    public String updateTrainStatus(Long trainId, String status) {
        Train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new RuntimeException("Train not found"));
        
        if ("ACTIVE".equalsIgnoreCase(status)) {
            train.setStatus(Train.TrainStatus.ACTIVE);
        } else if ("INACTIVE".equalsIgnoreCase(status)) {
            train.setStatus(Train.TrainStatus.INACTIVE);
        } else if ("CANCELLED".equalsIgnoreCase(status)) {
            train.setStatus(Train.TrainStatus.CANCELLED);
        } else {
            throw new RuntimeException("Invalid status. Use ACTIVE, INACTIVE, or CANCELLED");
        }
        
        trainRepository.save(train);
        return "Train status updated to " + status;
    }
} 