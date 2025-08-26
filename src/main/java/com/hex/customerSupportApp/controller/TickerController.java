package com.hex.customerSupportApp.controller;

import com.hex.customerSupportApp.dto.TicketDtos;
import com.hex.customerSupportApp.security.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TickerController {

    private final TicketService ticketService;


    public TickerController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketDtos.TicketResponse> create(@Valid @RequestBody TicketDtos.CreateTicketRequest request) {
        return ResponseEntity.ok(ticketService.createTicket(request));
    }


    @GetMapping
    public ResponseEntity<List<TicketDtos.TicketResponse>> getAll() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

}
