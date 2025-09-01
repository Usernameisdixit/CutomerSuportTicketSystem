package com.hex.customerSupportApp.dto;

import java.time.LocalDateTime;

public record TicketHistoryDto(
        Long id,
        Long ticketId,
        String action,
        String oldValue,
        String newValue,
        LocalDateTime updatedAt,
        String updatedBy
) {
}
