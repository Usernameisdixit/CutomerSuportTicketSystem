package com.hex.customerSupportApp.repository;

import com.hex.customerSupportApp.entity.TicketHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Long> {

    List<TicketHistory> findByTicketIdOrderByUpdatedAtDesc(Long ticketId);
}
