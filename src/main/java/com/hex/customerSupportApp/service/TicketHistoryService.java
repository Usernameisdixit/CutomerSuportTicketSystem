package com.hex.customerSupportApp.service;

import com.hex.customerSupportApp.dto.TicketHistoryDto;
import com.hex.customerSupportApp.entity.Ticket;
import com.hex.customerSupportApp.entity.TicketHistory;
import com.hex.customerSupportApp.entity.User;
import com.hex.customerSupportApp.repository.TicketHistoryRepository;
import com.hex.customerSupportApp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TicketHistoryService {

    private final TicketHistoryRepository ticketHistoryRepository;
    private final UserRepository userRepository;


    public void logHistory(Ticket ticket, String action, String oldValue, String newValue, String comment, String username) {
        User updater = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        TicketHistory history = TicketHistory.builder()
                .ticket(ticket)
                .action(action)
                .oldValue(oldValue)
                .newValue(newValue)
                .comment(comment)
                .updatedAt(LocalDateTime.now())
                .updatedBy(updater)
                .build();

        ticketHistoryRepository.save(history);
    }

    public List<TicketHistoryDto> getHistory(Long ticketId) {
        return ticketHistoryRepository.findByTicketIdOrderByUpdatedAtDesc(ticketId)
                .stream().map(h -> new TicketHistoryDto(h.getId(),
                        h.getTicket().getId(),
                        h.getAction(),
                        h.getOldValue(),
                        h.getNewValue(),
                        h.getComment(),
                        h.getUpdatedAt(),
                        h.getUpdatedBy().getUsername())).toList();

    }


}
