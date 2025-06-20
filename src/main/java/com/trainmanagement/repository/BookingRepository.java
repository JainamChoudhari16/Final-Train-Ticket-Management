package com.trainmanagement.repository;

import com.trainmanagement.model.Booking;
import com.trainmanagement.model.User;
import com.trainmanagement.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByUser(User user);
    
    List<Booking> findByUserAndStatus(User user, Booking.BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.user = :user AND b.journeyDate >= :currentDate ORDER BY b.journeyDate")
    List<Booking> findUpcomingBookings(@Param("user") User user, @Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT b FROM Booking b WHERE b.user = :user AND b.journeyDate < :currentDate ORDER BY b.journeyDate DESC")
    List<Booking> findPastBookings(@Param("user") User user, @Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT b FROM Booking b WHERE b.train.id = :trainId AND FUNCTION('YEAR', b.journeyDate) = FUNCTION('YEAR', :journeyDate) " +
           "AND FUNCTION('MONTH', b.journeyDate) = FUNCTION('MONTH', :journeyDate) " +
           "AND FUNCTION('DAY', b.journeyDate) = FUNCTION('DAY', :journeyDate)")
    List<Booking> findByTrainIdAndJourneyDate(@Param("trainId") Long trainId, @Param("journeyDate") LocalDateTime journeyDate);
    
    List<Booking> findByTrain(Train train);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.train.id = :trainId AND FUNCTION('YEAR', b.journeyDate) = FUNCTION('YEAR', :journeyDate) " +
           "AND FUNCTION('MONTH', b.journeyDate) = FUNCTION('MONTH', :journeyDate) " +
           "AND FUNCTION('DAY', b.journeyDate) = FUNCTION('DAY', :journeyDate) AND b.status = 'CONFIRMED'")
    long countConfirmedBookingsForTrain(@Param("trainId") Long trainId, @Param("journeyDate") LocalDateTime journeyDate);
    
    void deleteByTrain(Train train);
} 