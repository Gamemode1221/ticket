package com.example.ticket.service;

import com.example.ticket.api.dto.TicketCreateRequest;
import com.example.ticket.api.dto.TicketUpdateRequest;
import com.example.ticket.api.error.NotFoundException;
import com.example.ticket.domain.Ticket;
import com.example.ticket.domain.TicketRepository;
import com.example.ticket.domain.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Long create(TicketCreateRequest req) {
        Ticket t = new Ticket(req.title, req.content);
        ticketRepository.save(t);
        return t.getId();
    }

    @Transactional(readOnly = true)
    public Ticket get(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Ticket> searchAll(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Ticket> searchTicketByStatus(TicketStatus status, Pageable pageable) {
        return ticketRepository.findByStatus(status, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Ticket> searchTicketsByKeyword(String q, Pageable pageable) {
        return ticketRepository.findByKeyword(q, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Ticket> searchByStatusAndKeyword(TicketStatus status, String q, Pageable pageable) {
        return ticketRepository.searchByStatusAndKeyword(status, q, pageable);
    }


    public void update(Long id, TicketUpdateRequest req) {
        Ticket t = get(id);
        t.update(req.title, req.content);
    }

    public void delete(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new NotFoundException("Ticket not found: " + id);
        }
        ticketRepository.deleteById(id);
    }

    public void changeStatus(Long id, TicketStatus status) {
        Ticket t = get(id);
        t.changeStatus(status);
    }
}
