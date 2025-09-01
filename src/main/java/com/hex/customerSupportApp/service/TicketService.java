package com.hex.customerSupportApp.service;

import com.hex.customerSupportApp.dto.TicketDtos;
import com.hex.customerSupportApp.entity.Ticket;
import com.hex.customerSupportApp.entity.TicketStatus;
import com.hex.customerSupportApp.entity.User;
import com.hex.customerSupportApp.repository.TicketHistoryRepository;
import com.hex.customerSupportApp.repository.TicketRepository;
import com.hex.customerSupportApp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketHistoryService ticketHistoryService;


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


    public List<TicketDtos.TicketResponse> getCustomerTickets() {
        return ticketRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());

    }

    public List<TicketDtos.TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());

    }

    public TicketDtos.TicketResponse updateTicket(Long id, TicketDtos.UpdateTicketRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Ticket ticket = ticketRepository.findById(id).orElseThrow();

        if (!isAdmin && auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_AGENT"))) {
            throw new RuntimeException("You are not allowed to update tickets.");
        }

//in the req we are assigning the assignedTo value so we have kept !=null
        if (req.assignedTo() != null) {
            String oldAssignee = ticket.getAssignedTo() != null ? ticket.getAssignedTo().getUsername() : null;
            if (isAdmin) {
                User assignee = userRepository.findByUsername(req.assignedTo()).orElseThrow(() -> new RuntimeException("User not found"));
                ticket.setAssignedTo(assignee);
            } else {


                if (!req.assignedTo().equals(currentUser)) {
                    throw new RuntimeException(("Agents can only assign tickets to themselves."));
                }
                User assignee = userRepository.findByUsername(currentUser).orElseThrow();
                ticket.setAssignedTo(assignee);
            }

            String newAssignee = ticket.getAssignedTo() != null ? ticket.getAssignedTo().getUsername() : null;
            ticketHistoryService.logHistory(ticket, "ASSIGNED_CHANGE", oldAssignee, newAssignee, req.comment(), currentUser);
        }
        if (req.status() != null && !req.status().equals(ticket.getStatus())) {
            String oldStatus = ticket.getStatus() != null ? ticket.getStatus().name() : null;
            ticket.setStatus(req.status());
            String newStaus = ticket.getStatus() != null ? ticket.getStatus().name() : null;

            ticketHistoryService.logHistory(ticket, "STATUS_CHANGED", oldStatus, newStaus, req.comment(), currentUser);

        }
        return mapToResponse(ticketRepository.save(ticket));
    }


    private TicketDtos.TicketResponse mapToResponse(Ticket ticket) {
        return new TicketDtos.TicketResponse(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getStatus().name(), ticket.getPriority().name(), ticket.getCreatedBy() != null ? ticket.getCreatedBy().getUsername() : null, ticket.getAssignedTo() != null ? ticket.getAssignedTo().getUsername() : null);
    }


}
