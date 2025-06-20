package com.trainmanagement.model.dto;

import com.trainmanagement.model.Booking;
import java.time.LocalDateTime;

public class BookingResponseDto {
    private Long id;
    private UserDto user;
    private PassengerDto passenger;
    private LocalDateTime journeyDate;
    private String seatNumber;
    private LocalDateTime bookingDate;
    private Double totalAmount;
    private String status;
    private TicketResponseDto ticket;

    public BookingResponseDto() {}

    public static BookingResponseDto fromBooking(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        
        if (booking.getUser() != null) {
            UserDto userDto = new UserDto();
            userDto.setId(booking.getUser().getId());
            userDto.setName(booking.getUser().getName());
            userDto.setEmail(booking.getUser().getEmail());
            userDto.setContactNumber(booking.getUser().getContactNumber());
            userDto.setAddress(booking.getUser().getAddress());
            dto.setUser(userDto);
        }
        
        if (booking.getPassenger() != null) {
            PassengerDto passengerDto = new PassengerDto();
            passengerDto.setId(booking.getPassenger().getId());
            passengerDto.setName(booking.getPassenger().getName());
            passengerDto.setAge(booking.getPassenger().getAge());
            passengerDto.setGender(booking.getPassenger().getGender().toString());
            dto.setPassenger(passengerDto);
        }
        
        dto.setJourneyDate(booking.getJourneyDate());
        dto.setSeatNumber(booking.getSeatNumber());
        dto.setBookingDate(booking.getBookingDate());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setStatus(booking.getStatus() != null ? booking.getStatus().toString() : null);
        
        if (booking.getTicket() != null) {
            dto.setTicket(TicketResponseDto.fromTicket(booking.getTicket()));
        }
        
        return dto;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public PassengerDto getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerDto passenger) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TicketResponseDto getTicket() {
        return ticket;
    }

    public void setTicket(TicketResponseDto ticket) {
        this.ticket = ticket;
    }
} 