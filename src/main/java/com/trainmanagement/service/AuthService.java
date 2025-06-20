package com.trainmanagement.service;

import com.trainmanagement.model.Login;
import com.trainmanagement.model.User;
import com.trainmanagement.model.dto.LoginDto;
import com.trainmanagement.model.dto.UserRegistrationDto;
import com.trainmanagement.repository.LoginRepository;
import com.trainmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public String registerUser(UserRegistrationDto registrationDto) {
        // Check if email already exists
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create user
        User user = new User();
        user.setName(registrationDto.getName());
        user.setEmail(registrationDto.getEmail());
        user.setContactNumber(registrationDto.getContactNumber());
        user.setAddress(registrationDto.getAddress());
        
        // Create login record
        Login login = new Login();
        login.setEmail(registrationDto.getEmail());
        login.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        login.setUserType(Login.UserType.CUSTOMER);
        login.setStatus(Login.Status.ACTIVE);
        
        // Set bidirectional relationship
        user.setLogin(login);
        login.setUser(user);

        // Save user, which will also save login due to cascade
        User savedUser = userRepository.save(user);

        return "User registered successfully with ID: " + savedUser.getId();
    }

    @Transactional
    public String registerAdmin(LoginDto adminDto) {
        // Check if email already exists
        if (loginRepository.existsByEmail(adminDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create admin login record
        Login login = new Login();
        login.setEmail(adminDto.getEmail());
        login.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        login.setUserType(Login.UserType.ADMIN);
        login.setStatus(Login.Status.ACTIVE);
        
        loginRepository.save(login);

        return "Admin registered successfully";
    }

    public String authenticateUser(LoginDto loginDto) {
        Optional<Login> loginOpt = loginRepository.findByEmail(loginDto.getEmail());
        
        if (loginOpt.isEmpty()) {
            throw new RuntimeException("User not found with this email");
        }

        Login login = loginOpt.get();
        
        // Check if account is active
        if (login.getStatus() != Login.Status.ACTIVE) {
            throw new RuntimeException("Account is deactivated. Please contact administrator.");
        }

        // Verify password
        if (!passwordEncoder.matches(loginDto.getPassword(), login.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Return user type for frontend to handle different dashboards
        return "Login successful. User type: " + login.getUserType();
    }

    @Transactional
    public String activateAccount(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        Optional<Login> loginOpt = loginRepository.findByEmail(user.getEmail());
        
        if (loginOpt.isEmpty()) {
            throw new RuntimeException("Login record not found");
        }

        Login login = loginOpt.get();
        
        if (login.getStatus() == Login.Status.ACTIVE) {
            throw new RuntimeException("User is already active");
        }

        login.setStatus(Login.Status.ACTIVE);
        loginRepository.save(login);

        return "Account activated successfully";
    }
} 