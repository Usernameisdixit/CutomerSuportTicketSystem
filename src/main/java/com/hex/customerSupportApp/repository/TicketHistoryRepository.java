package com.hex.customerSupportApp.repository;

import com.hex.customerSupportApp.entity.TicketHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketHistoryRepository extends JpaRepository<TicketHistory,Long> {
}
