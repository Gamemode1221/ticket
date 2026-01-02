package com.example.ticket.api.dto;

import com.example.ticket.domain.Ticket;
import com.example.ticket.domain.TicketStatus;

import java.time.LocalDateTime;

public class TicketResponse {
    public Long id;
    public String title;
    public String content;
    public TicketStatus status;
    public LocalDateTime createdAt;

    public static TicketResponse from(Ticket t) {
        TicketResponse r = new TicketResponse();
        r.id = t.getId();
        r.title = t.getTitle();
        r.content = t.getContent();
        r.status = t.getStatus();
        r.createdAt = t.getCreatedAt();
        return r;
    }

}
