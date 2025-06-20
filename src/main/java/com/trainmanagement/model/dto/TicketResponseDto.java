package com.trainmanagement.model.dto;

import com.trainmanagement.model.Ticket;
import java.time.LocalDateTime;

public class TicketResponseDto {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;

    public TicketResponseDto() {}

    public static TicketResponseDto fromTicket(Ticket ticket) {
        TicketResponseDto dto = new TicketResponseDto();
        dto.setId(ticket.getId());
        dto.setStartDate(ticket.getStartDate());
        dto.setEndDate(ticket.getEndDate());
        dto.setStatus(ticket.getStatus() != null ? ticket.getStatus().toString() : null);
        return dto;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 