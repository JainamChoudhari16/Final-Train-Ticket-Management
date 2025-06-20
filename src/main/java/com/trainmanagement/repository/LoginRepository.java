package com.trainmanagement.repository;

import com.trainmanagement.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
    
    Optional<Login> findByEmail(String email);
    
    Optional<Login> findByEmailAndUserType(String email, Login.UserType userType);
    
    boolean existsByEmail(String email);
} 