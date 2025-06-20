package com.trainmanagement.repository;

import com.trainmanagement.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
    
    List<Train> findBySourceStationAndDestinationStation(String sourceStation, String destinationStation);
    
    List<Train> findBySourceStationAndDestinationStationAndDepartureTimeBetween(
            String sourceStation, String destinationStation, LocalDateTime startTime, LocalDateTime endTime);
    
    List<Train> findByStatus(Train.TrainStatus status);
    
    @Query("SELECT t FROM Train t WHERE t.availableSeats > 0 AND t.status = 'ACTIVE'")
    List<Train> findAvailableTrains();
    
    @Query("SELECT t FROM Train t WHERE t.sourceStation = :source AND t.destinationStation = :destination " +
           "AND t.departureTime >= :date AND t.availableSeats > 0 AND t.status = 'ACTIVE'")
    List<Train> findAvailableTrainsByRouteAndDate(@Param("source") String source, 
                                                  @Param("destination") String destination, 
                                                  @Param("date") LocalDateTime date);
} 