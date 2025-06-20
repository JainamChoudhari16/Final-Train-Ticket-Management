package com.trainmanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "person_id")
    private User person;
    
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Train location;
    
    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;
    
    @NotNull(message = "End date is required")
    private LocalDateTime endDate;
    
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
    
    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
    
    public enum TicketStatus {
        ACTIVE, CANCELLED, COMPLETED
    }
    
    // Constructors
    public Ticket() {}
    
    public Ticket(User person, Train location, LocalDateTime startDate, 
                  LocalDateTime endDate, Booking booking) {
        this.person = person;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.booking = booking;
        this.status = TicketStatus.ACTIVE;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getPerson() {
        return person;
    }
    
    public void setPerson(User person) {
        this.person = person;
    }
    
    public Train getLocation() {
        return location;
    }
    
    public void setLocation(Train location) {
        this.location = location;
    }
    
    public LocalDateTime getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    
    public LocalDateTime getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    
    public TicketStatus getStatus() {
        return status;
    }
    
    public void setStatus(TicketStatus status) {
        this.status = status;
    }
    
    public Booking getBooking() {
        return booking;
    }
    
    public void setBooking(Booking booking) {
        this.booking = booking;
    }
} 