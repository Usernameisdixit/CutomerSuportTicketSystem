package com.hex.customerSupportApp.dto;

import com.hex.customerSupportApp.entity.Priority;
import com.hex.customerSupportApp.entity.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TicketDtos {

    public record CreateTicketRequest(
            @NotBlank String title,
            @NotBlank String description,
            @NotNull Priority priority
    ) {
    }


    public record UpdateTicketRequest(String title,
                                      String description,
                                      TicketStatus status,
                                      String priority,
                                      String assignedTo) {
    }


    public record TicketResponse(Long id, String title, String description, String status, String priority,
                                 String createdBy,
                                 String assignedTo) {
    }

}
