package com.example.ticket.api;

import com.example.ticket.api.dto.TicketCreateRequest;
import com.example.ticket.api.dto.TicketResponse;
import com.example.ticket.api.dto.TicketStatusChangeRequest;
import com.example.ticket.api.dto.TicketUpdateRequest;
import com.example.ticket.domain.Ticket;
import com.example.ticket.domain.TicketStatus;
import com.example.ticket.service.TicketService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public Long create(@Valid @RequestBody TicketCreateRequest req) {
        return ticketService.create(req);
    }

    @GetMapping("/{id}")
    public TicketResponse get(@PathVariable Long id) {
        Ticket t = ticketService.get(id);
        return TicketResponse.from(t);
    }

    @GetMapping
    public Page<TicketResponse> list(
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) String q,
            @ParameterObject Pageable pageable
    ) {
        return ticketService.search(status, q, pageable)
                .map(TicketResponse::from);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody TicketUpdateRequest req) {
        ticketService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ticketService.delete(id);
    }

    @PatchMapping("/{id}/status")
    public void changeStatus(@PathVariable Long id, @Valid @RequestBody TicketStatusChangeRequest req) {
        ticketService.changeStatus(id, req.status);
    }

}
