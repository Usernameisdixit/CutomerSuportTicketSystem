package com.hex.customerSupportApp.repository;

import com.hex.customerSupportApp.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,Long> {

}
