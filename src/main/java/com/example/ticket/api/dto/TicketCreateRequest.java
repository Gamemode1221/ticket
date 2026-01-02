package com.example.ticket.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TicketCreateRequest {
    @NotBlank
    @Size(max = 200)
    public String title;

    @NotBlank
    @Size(max = 2000)
    public String content;
}
