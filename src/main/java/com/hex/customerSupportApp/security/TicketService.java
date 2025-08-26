package com.hex.customerSupportApp.security;

import com.hex.customerSupportApp.dto.TicketDtos;
import com.hex.customerSupportApp.entity.Ticket;
import com.hex.customerSupportApp.entity.TicketStatus;
import com.hex.customerSupportApp.entity.User;
import com.hex.customerSupportApp.repository.TicketRepository;
import com.hex.customerSupportApp.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public TicketDtos.TicketResponse createTicket(TicketDtos.CreateTicketRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User creator = userRepository.findByUsername(auth.getName()).orElseThrow();

        Ticket ticket = Ticket.builder().title(request.title())
                .description(request.description())
                .status(TicketStatus.OPEN)
                .priority(request.priority())
                .createdBy(creator)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Ticket saved = ticketRepository.save(ticket);
        return mapToResponse(saved);

    }


    public List<TicketDtos.TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());

    }

    private TicketDtos.TicketResponse mapToResponse(Ticket ticket) {
        return new TicketDtos.TicketResponse(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getStatus().name(), ticket.getPriority().name(), ticket.getCreatedBy() != null ? ticket.getCreatedBy().getUsername() : null, ticket.getAssignedTo() != null ? ticket.getAssignedTo().getUsername() : null);
    }


}
