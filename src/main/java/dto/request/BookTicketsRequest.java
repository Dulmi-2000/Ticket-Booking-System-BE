package com.example.bookingSystem.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookTicketsRequest {
    @NotNull
    private Long eventId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
