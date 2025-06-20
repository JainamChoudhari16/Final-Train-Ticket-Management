package com.trainmanagement.service;

import com.trainmanagement.model.Booking;
import com.trainmanagement.model.Passenger;
import com.trainmanagement.model.Ticket;
import com.trainmanagement.model.Train;
import com.trainmanagement.model.User;
import com.trainmanagement.model.dto.BookingDto;
import com.trainmanagement.model.dto.BookingResponseDto;
import com.trainmanagement.repository.BookingRepository;
import com.trainmanagement.repository.PassengerRepository;
import com.trainmanagement.repository.TicketRepository;
import com.trainmanagement.repository.TrainRepository;
import com.trainmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public Object getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(BookingResponseDto::fromBooking)
                .collect(Collectors.toList());
    }

    public Object getBookingById(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        
        if (bookingOpt.isEmpty()) {
            throw new RuntimeException("Booking not found");
        }
        
        return BookingResponseDto.fromBooking(bookingOpt.get());
    }

    public Object createBooking(BookingDto bookingDto) {
        // Validate entities
        Train train = trainRepository.findById(bookingDto.getTrainId())
                .orElseThrow(() -> new RuntimeException("Train not found"));
        
        User user = userRepository.findById(bookingDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Create a new Passenger from the DTO
        Passenger passenger = new Passenger();
        passenger.setName(bookingDto.getPassengerName());
        passenger.setAge(bookingDto.getPassengerAge());
        passenger.setGender(bookingDto.getPassengerGender());
        passenger.setUser(user); // Associate passenger with the user
        Passenger savedPassenger = passengerRepository.save(passenger);

        // Parse journey date
        LocalDateTime journeyDate;
        try {
            // Parse date in yyyy-MM-dd format and set time to start of day
            journeyDate = LocalDateTime.parse(bookingDto.getJourneyDate() + "T00:00:00");
        } catch (Exception e) {
            try {
                // Try alternative format if the first one fails
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                journeyDate = LocalDate.parse(bookingDto.getJourneyDate(), formatter).atStartOfDay();
            } catch (Exception ex) {
                throw new RuntimeException("Invalid journey date format. Use yyyy-MM-dd");
            }
        }

        // Check if journey date is not in the past
        if (journeyDate.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Journey date cannot be in the past");
        }

        // Check seat availability
        List<Booking> existingBookings = bookingRepository.findByTrainIdAndJourneyDate(train.getId(), journeyDate);
        boolean seatTaken = existingBookings.stream()
                .anyMatch(booking -> booking.getSeatNumber().equals(bookingDto.getSeatNumber()) && 
                        booking.getStatus() == Booking.BookingStatus.CONFIRMED);
        
        if (seatTaken) {
            throw new RuntimeException("Selected seat is already booked");
        }

        if (train.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available on this train");
        }
        
        // Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTrain(train);
        booking.setPassenger(savedPassenger);
        booking.setJourneyDate(journeyDate);
        booking.setSeatNumber(bookingDto.getSeatNumber());
        booking.setBookingDate(LocalDateTime.now());
        booking.setTotalAmount(bookingDto.getTotalAmount());
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        
        Booking savedBooking = bookingRepository.save(booking);
        
        // Update train available seats
        train.setAvailableSeats(train.getAvailableSeats() - 1);
        trainRepository.save(train);
        
        // Create ticket
        Ticket ticket = new Ticket(user, train, journeyDate, train.getArrivalTime(), savedBooking);
        ticketRepository.save(ticket);
        
        return BookingResponseDto.fromBooking(savedBooking);
    }

    public Object bookTicket(Map<String, Object> bookingRequest) {
        // Extract booking details from request
        Long trainId = Long.parseLong(bookingRequest.get("trainId").toString());
        Long userId = Long.parseLong(bookingRequest.get("userId").toString());
        Long passengerId = Long.parseLong(bookingRequest.get("passengerId").toString());
        String journeyDateStr = bookingRequest.get("journeyDate").toString();
        String seatNumber = bookingRequest.get("seatNumber").toString();
        
        // Validate entities
        Train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new RuntimeException("Train not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));
        
        // Parse journey date
        LocalDateTime journeyDate;
        try {
            journeyDate = LocalDateTime.parse(journeyDateStr + "T00:00:00");
        } catch (Exception e) {
            throw new RuntimeException("Invalid journey date format. Use yyyy-MM-dd");
        }
        
        // Check if journey date is not in the past
        if (journeyDate.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Journey date cannot be in the past");
        }
        
        // Check seat availability
        if (train.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available on this train");
        }
        
        // Create booking
        Booking booking = new Booking(user, train, passenger, journeyDate, seatNumber, train.getPrice());
        Booking savedBooking = bookingRepository.save(booking);
        
        // Update train available seats
        train.setAvailableSeats(train.getAvailableSeats() - 1);
        trainRepository.save(train);
        
        // Create ticket
        Ticket ticket = new Ticket(user, train, journeyDate, train.getArrivalTime(), savedBooking);
        ticketRepository.save(ticket);
        
        Map<String, Object> result = new HashMap<>();
        result.put("bookingId", savedBooking.getId());
        result.put("ticketId", ticket.getId());
        result.put("status", "CONFIRMED");
        result.put("message", "Ticket booked successfully");
        
        return result;
    }

    public Object updateBooking(Long bookingId, BookingDto bookingDto) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        
        if (bookingOpt.isEmpty()) {
            throw new RuntimeException("Booking not found");
        }
        
        Booking booking = bookingOpt.get();
        
        // Update fields
        booking.setSeatNumber(bookingDto.getSeatNumber());
        booking.setTotalAmount(bookingDto.getTotalAmount());
        
        Booking updatedBooking = bookingRepository.save(booking);
        return BookingResponseDto.fromBooking(updatedBooking);
    }

    public Object getBookingStatus(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        
        if (bookingOpt.isEmpty()) {
            throw new RuntimeException("Booking not found");
        }
        
        Booking booking = bookingOpt.get();
        
        Map<String, Object> status = new HashMap<>();
        status.put("bookingId", booking.getId());
        status.put("status", booking.getStatus());
        status.put("trainNumber", booking.getTrain().getTrainNumber());
        status.put("journeyDate", booking.getJourneyDate());
        status.put("seatNumber", booking.getSeatNumber());
        
        return status;
    }

    public Object fetchTicketDetails(Long ticketId) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        
        if (ticketOpt.isEmpty()) {
            throw new RuntimeException("Ticket not found");
        }
        
        Ticket ticket = ticketOpt.get();
        
        Map<String, Object> ticketDetails = new HashMap<>();
        ticketDetails.put("ticketId", ticket.getId());
        ticketDetails.put("trainNumber", ticket.getLocation().getTrainNumber());
        ticketDetails.put("trainName", ticket.getLocation().getTrainName());
        ticketDetails.put("sourceStation", ticket.getLocation().getSourceStation());
        ticketDetails.put("destinationStation", ticket.getLocation().getDestinationStation());
        ticketDetails.put("departureTime", ticket.getLocation().getDepartureTime());
        ticketDetails.put("arrivalTime", ticket.getEndDate());
        ticketDetails.put("journeyDate", ticket.getStartDate());
        ticketDetails.put("status", ticket.getStatus());
        ticketDetails.put("bookingId", ticket.getBooking().getId());
        
        return ticketDetails;
    }

    public Object cancelBooking(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        
        if (bookingOpt.isEmpty()) {
            throw new RuntimeException("Booking not found");
        }
        
        Booking booking = bookingOpt.get();
        
        // Check if booking can be cancelled
        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }
        
        if (booking.getStatus() == Booking.BookingStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel completed booking");
        }
        
        // Cancel booking
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        Booking cancelledBooking = bookingRepository.save(booking);
        
        // Update train available seats
        Train train = booking.getTrain();
        train.setAvailableSeats(train.getAvailableSeats() + 1);
        trainRepository.save(train);
        
        return BookingResponseDto.fromBooking(cancelledBooking);
    }

    public String deleteBooking(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        
        if (bookingOpt.isEmpty()) {
            throw new RuntimeException("Booking not found");
        }
        
        Booking booking = bookingOpt.get();
        
        // Delete related ticket first
        Optional<Ticket> relatedTicket = ticketRepository.findByBooking(booking);
        if (relatedTicket.isPresent()) {
            ticketRepository.delete(relatedTicket.get());
        }
        
        // Update train available seats if booking was confirmed
        if (booking.getStatus() == Booking.BookingStatus.CONFIRMED) {
            Train train = booking.getTrain();
            train.setAvailableSeats(train.getAvailableSeats() + 1);
            trainRepository.save(train);
        }
        
        bookingRepository.deleteById(bookingId);
        return "Booking deleted successfully";
    }

    public Object getUserBookings(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User user = userOpt.get();
        List<Booking> bookings = bookingRepository.findByUser(user);
        return bookings.stream()
                .map(BookingResponseDto::fromBooking)
                .collect(Collectors.toList());
    }

    public Object getBookingsByUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User user = userOpt.get();
        List<Booking> bookings = bookingRepository.findByUser(user);
        return bookings.stream()
                .map(BookingResponseDto::fromBooking)
                .collect(Collectors.toList());
    }

    public Object getBookingsByTrain(Long trainId) {
        Optional<Train> trainOpt = trainRepository.findById(trainId);
        
        if (trainOpt.isEmpty()) {
            throw new RuntimeException("Train not found");
        }
        
        // This would need a custom query in the repository
        // For now, we'll return all bookings and filter by train
        List<Booking> allBookings = bookingRepository.findAll();
        List<Booking> trainBookings = allBookings.stream()
                .filter(booking -> booking.getTrain().getId().equals(trainId))
                .collect(Collectors.toList());
        
        return trainBookings.stream()
                .map(BookingResponseDto::fromBooking)
                .collect(Collectors.toList());
    }

    public Object reserveSeat(Map<String, Object> reservationRequest) {
        // Extract reservation details
        Long trainId = Long.parseLong(reservationRequest.get("trainId").toString());
        String journeyDateStr = reservationRequest.get("journeyDate").toString();
        String seatNumber = reservationRequest.get("seatNumber").toString();
        
        // Validate train
        Train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new RuntimeException("Train not found"));
        
        // Parse journey date
        LocalDateTime journeyDate;
        try {
            journeyDate = LocalDateTime.parse(journeyDateStr + "T00:00:00");
        } catch (Exception e) {
            throw new RuntimeException("Invalid journey date format. Use yyyy-MM-dd");
        }
        
        // Check if journey date is not in the past
        if (journeyDate.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Journey date cannot be in the past");
        }
        
        // Check seat availability
        if (train.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available on this train");
        }
        
        // Check if seat is already reserved
        List<Booking> existingBookings = bookingRepository.findByTrainIdAndJourneyDate(trainId, journeyDate);
        boolean seatReserved = existingBookings.stream()
                .anyMatch(booking -> booking.getSeatNumber().equals(seatNumber) && 
                        booking.getStatus() == Booking.BookingStatus.CONFIRMED);
        
        if (seatReserved) {
            throw new RuntimeException("Seat is already reserved");
        }
        
        // Reserve seat (update train available seats)
        train.setAvailableSeats(train.getAvailableSeats() - 1);
        trainRepository.save(train);
        
        Map<String, Object> result = new HashMap<>();
        result.put("trainId", trainId);
        result.put("seatNumber", seatNumber);
        result.put("journeyDate", journeyDateStr);
        result.put("status", "RESERVED");
        result.put("message", "Seat reserved successfully");
        
        return result;
    }
} 