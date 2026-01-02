package com.example.ticket.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("""
        select t
        from Ticket t
        where (:status is null or :q = '' or
                lower(t.title) like lower(concat('%', :q, '%')) or
                lower(t.content) like lower(concat('%', :q, '%')))
        """)
    Page<Ticket> search(@Param("status") TicketStatus status,
                        @Param("q") String q,
                        Pageable pageable);
}
