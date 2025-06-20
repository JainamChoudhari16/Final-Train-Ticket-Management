package com.trainmanagement.repository;

import com.trainmanagement.model.Passenger;
import com.trainmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    
    List<Passenger> findByUser(User user);
    
    List<Passenger> findByUserAndAgeGreaterThan(User user, Integer age);
} 