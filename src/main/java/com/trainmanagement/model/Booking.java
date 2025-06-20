package com.trainmanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "train_id")
    private Train train;
    
    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;
    
    @JsonManagedReference
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Ticket ticket;
    
    @NotNull(message = "Journey date is required")
    private LocalDateTime journeyDate;
    
    @NotNull(message = "Seat number is required")
    private String seatNumber;
    
    @NotNull(message = "Booking date is required")
    private LocalDateTime bookingDate;
    
    @NotNull(message = "Total amount is required")
    private Double totalAmount;
    
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    
    public enum BookingStatus {
        CONFIRMED, CANCELLED, COMPLETED
    }
    
    // Constructors
    public Booking() {}
    
    public Booking(User user, Train train, Passenger passenger, LocalDateTime journeyDate, 
                   String seatNumber, Double totalAmount) {
        this.user = user;
        this.train = train;
        this.passenger = passenger;
        this.journeyDate = journeyDate;
        this.seatNumber = seatNumber;
        this.bookingDate = LocalDateTime.now();
        this.totalAmount = totalAmount;
        this.status = BookingStatus.CONFIRMED;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Train getTrain() {
        return train;
    }
    
    public void setTrain(Train train) {
        this.train = train;
    }
    
    public Passenger getPassenger() {
        return passenger;
    }
    
    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }
    
    public LocalDateTime getJourneyDate() {
        return journeyDate;
    }
    
    public void setJourneyDate(LocalDateTime journeyDate) {
        this.journeyDate = journeyDate;
    }
    
    public String getSeatNumber() {
        return seatNumber;
    }
    
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    
    public LocalDateTime getBookingDate() {
        return bookingDate;
    }
    
    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    public Double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public BookingStatus getStatus() {
        return status;
    }
    
    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
} 