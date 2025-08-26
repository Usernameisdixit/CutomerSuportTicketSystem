package com.hex.customerSupportApp.dto;

import com.hex.customerSupportApp.entity.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TicketDtos {

    public record CreateTicketRequest(
            @NotBlank String title,
            @NotBlank String description,
            @NotNull Priority priority
    ) {
    }


    public record UpdateTicketRequest(@NotNull Long id, String title, String description, String status,
                                      Long assignedTo) {
    }


    public record TicketResponse(Long id, String title, String description, String status, String priority,
                                 String createdBy,
                                 String assignedTo) {
    }

}
