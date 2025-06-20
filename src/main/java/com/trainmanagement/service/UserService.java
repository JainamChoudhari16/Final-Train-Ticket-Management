package com.trainmanagement.service;

import com.trainmanagement.model.Booking;
import com.trainmanagement.model.Login;
import com.trainmanagement.model.User;
import com.trainmanagement.model.dto.UserDto;
import com.trainmanagement.repository.BookingRepository;
import com.trainmanagement.repository.LoginRepository;
import com.trainmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public Object getUserProfile(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        Optional<Login> loginOpt = loginRepository.findByEmail(email);
        
        Map<String, Object> profile = new HashMap<>();
        profile.put("id", user.getId());
        profile.put("name", user.getName());
        profile.put("email", user.getEmail());
        profile.put("contactNumber", user.getContactNumber());
        profile.put("address", user.getAddress());
        profile.put("status", loginOpt.map(Login::getStatus).orElse(null));
        profile.put("userType", loginOpt.map(Login::getUserType).orElse(null));
        
        return profile;
    }

    public Object getUserById(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        return userOpt.get();
    }

    public Object getAllUsers() {
        return userRepository.findAll();
    }

    public Object createUser(UserDto userDto) {
        // Check if email already exists
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Create user
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setContactNumber(userDto.getContactNumber());
        user.setAddress(userDto.getAddress());
        
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public Object getBookingHistory(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        List<Booking> pastBookings = bookingRepository.findPastBookings(user, LocalDateTime.now());
        
        return pastBookings;
    }

    public Object getJourneyInfo(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        List<Booking> upcomingBookings = bookingRepository.findUpcomingBookings(user, LocalDateTime.now());
        
        Map<String, Object> journeyInfo = new HashMap<>();
        journeyInfo.put("upcomingJourneys", upcomingBookings);
        journeyInfo.put("totalUpcoming", upcomingBookings.size());
        
        return journeyInfo;
    }

    public String updateUserProfile(String email, UserDto userDto) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        
        // Update fields
        user.setName(userDto.getName());
        user.setContactNumber(userDto.getContactNumber());
        user.setAddress(userDto.getAddress());
        
        userRepository.save(user);
        return "Profile updated successfully";
    }

    public Object updateUser(Long userId, UserDto userDto) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        
        // Update fields
        user.setName(userDto.getName());
        user.setContactNumber(userDto.getContactNumber());
        user.setAddress(userDto.getAddress());
        
        // Check email uniqueness if changed
        if (!userDto.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        user.setEmail(userDto.getEmail());
        
        User updatedUser = userRepository.save(user);
        return updatedUser;
    }

    public String deleteUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User user = userOpt.get();
        
        // Delete associated login record
        Optional<Login> loginOpt = loginRepository.findByEmail(user.getEmail());
        loginOpt.ifPresent(loginRepository::delete);
        
        // Delete user
        userRepository.deleteById(userId);
        return "User deleted successfully";
    }

    public Object getUpcomingTrips(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        List<Booking> upcomingBookings = bookingRepository.findUpcomingBookings(user, LocalDateTime.now());
        
        return upcomingBookings;
    }
} 