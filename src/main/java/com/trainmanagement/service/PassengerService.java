package com.trainmanagement.service;

import com.trainmanagement.model.Passenger;
import com.trainmanagement.model.User;
import com.trainmanagement.model.dto.PassengerDto;
import com.trainmanagement.repository.PassengerRepository;
import com.trainmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private UserRepository userRepository;

    public Object getAllPassengers() {
        return passengerRepository.findAll();
    }

    public Object createPassenger(PassengerDto passengerDto) {
        // Validate user
        User user = userRepository.findById(passengerDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Validate gender
        if (!passengerDto.getGender().matches("^(MALE|FEMALE|OTHER)$")) {
            throw new RuntimeException("Invalid gender. Use MALE, FEMALE, or OTHER");
        }
        
        // Create passenger
        Passenger passenger = new Passenger(passengerDto.getName(), passengerDto.getAge(), 
                passengerDto.getGender(), user);
        Passenger savedPassenger = passengerRepository.save(passenger);
        
        return savedPassenger;
    }

    public Object getPassengerById(Long passengerId) {
        Optional<Passenger> passengerOpt = passengerRepository.findById(passengerId);
        
        if (passengerOpt.isEmpty()) {
            throw new RuntimeException("Passenger not found");
        }
        
        return passengerOpt.get();
    }

    public Object updatePassenger(Long passengerId, PassengerDto passengerDto) {
        Optional<Passenger> passengerOpt = passengerRepository.findById(passengerId);
        
        if (passengerOpt.isEmpty()) {
            throw new RuntimeException("Passenger not found");
        }
        
        Passenger passenger = passengerOpt.get();
        
        // Update fields
        passenger.setName(passengerDto.getName());
        passenger.setAge(passengerDto.getAge());
        passenger.setGender(passengerDto.getGender());
        
        Passenger updatedPassenger = passengerRepository.save(passenger);
        return updatedPassenger;
    }

    public String deletePassenger(Long passengerId) {
        Optional<Passenger> passengerOpt = passengerRepository.findById(passengerId);
        
        if (passengerOpt.isEmpty()) {
            throw new RuntimeException("Passenger not found");
        }
        
        passengerRepository.deleteById(passengerId);
        return "Passenger deleted successfully";
    }

    public Object getPassengersByUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User user = userOpt.get();
        return passengerRepository.findByUser(user);
    }

    public Object addPassengerDetails(Map<String, Object> passengerData) {
        // Extract passenger details
        String name = passengerData.get("name").toString();
        Integer age = Integer.parseInt(passengerData.get("age").toString());
        String gender = passengerData.get("gender").toString();
        Long userId = Long.parseLong(passengerData.get("userId").toString());
        
        // Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Validate gender
        if (!gender.matches("^(MALE|FEMALE|OTHER)$")) {
            throw new RuntimeException("Invalid gender. Use MALE, FEMALE, or OTHER");
        }
        
        // Create passenger
        Passenger passenger = new Passenger(name, age, gender, user);
        Passenger savedPassenger = passengerRepository.save(passenger);
        
        Map<String, Object> result = new HashMap<>();
        result.put("passengerId", savedPassenger.getId());
        result.put("name", savedPassenger.getName());
        result.put("age", savedPassenger.getAge());
        result.put("gender", savedPassenger.getGender());
        result.put("message", "Passenger details added successfully");
        
        return result;
    }

    public Object getPassengerDetails(Long passengerId) {
        Optional<Passenger> passengerOpt = passengerRepository.findById(passengerId);
        
        if (passengerOpt.isEmpty()) {
            throw new RuntimeException("Passenger not found");
        }
        
        return passengerOpt.get();
    }

    public Object getUserPassengers(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User user = userOpt.get();
        return passengerRepository.findByUser(user);
    }

    public String updatePassengerDetails(Long passengerId, Map<String, Object> passengerData) {
        Optional<Passenger> passengerOpt = passengerRepository.findById(passengerId);
        
        if (passengerOpt.isEmpty()) {
            throw new RuntimeException("Passenger not found");
        }
        
        Passenger passenger = passengerOpt.get();
        
        // Update fields if provided
        if (passengerData.containsKey("name")) {
            passenger.setName(passengerData.get("name").toString());
        }
        
        if (passengerData.containsKey("age")) {
            passenger.setAge(Integer.parseInt(passengerData.get("age").toString()));
        }
        
        if (passengerData.containsKey("gender")) {
            String gender = passengerData.get("gender").toString();
            if (!gender.matches("^(MALE|FEMALE|OTHER)$")) {
                throw new RuntimeException("Invalid gender. Use MALE, FEMALE, or OTHER");
            }
            passenger.setGender(gender);
        }
        
        passengerRepository.save(passenger);
        return "Passenger details updated successfully";
    }

    public Object getPassengersByAge(Integer age) {
        return passengerRepository.findByUserAndAgeGreaterThan(null, age);
    }
} 