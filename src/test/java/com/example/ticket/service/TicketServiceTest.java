package com.example.ticket.service;

import com.example.ticket.api.dto.TicketCreateRequest;
import com.example.ticket.api.error.NotFoundException;
import com.example.ticket.domain.Ticket;
import com.example.ticket.domain.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class TicketServiceTest {

    @Autowired TicketService ticketService;
    @Autowired TicketRepository ticketRepository;

    @Test
    void create_then_get_success() {
        // given
        TicketCreateRequest req = new TicketCreateRequest();
        req.title = "test title";
        req.content =  "test content";

        // when
        Long id = ticketService.create(req);

        // then
        Ticket found = ticketService.get(id);
        assertThat(found.getId()).isEqualTo(id);
        assertThat(found.getTitle()).isEqualTo("test title");
        assertThat(found.getContent()).isEqualTo("test content");
    }

    @Test
    void get_not_found() {
        assertThatThrownBy(() -> ticketService.get(999999L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void delete_not_found() {
        assertThatThrownBy(() -> ticketService.delete(999999L))
                .isInstanceOf(NotFoundException.class);
    }
}
