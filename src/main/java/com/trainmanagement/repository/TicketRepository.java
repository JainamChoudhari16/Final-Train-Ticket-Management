package com.trainmanagement.repository;

import com.trainmanagement.model.Booking;
import com.trainmanagement.model.Ticket;
import com.trainmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    List<Ticket> findByPerson(User person);
    
    List<Ticket> findByPersonAndStatus(User person, Ticket.TicketStatus status);
    
    List<Ticket> findByStatus(Ticket.TicketStatus status);
    
    Optional<Ticket> findByBooking(Booking booking);
    
    void deleteByBooking(Booking booking);
} 