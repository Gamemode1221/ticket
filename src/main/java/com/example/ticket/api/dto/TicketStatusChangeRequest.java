package com.example.ticket.api.dto;

import com.example.ticket.domain.TicketStatus;
import jakarta.validation.constraints.NotNull;

public class TicketStatusChangeRequest {
    @NotNull
    public TicketStatus status;
}
