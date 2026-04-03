package com.example.bookingSystem.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateEventRequest {
    @NotBlank
    private String title;

    private String description;

    private String venue;

    private String location;

    @NotBlank
    private String date;

    private String time;

    @NotNull
    @Min(0)
    private Integer priceCents;

    @NotNull
    @Min(1)
    private Integer totalTickets;

    private String imageUrl;

    private String category;

    private List<String> includedItems;

    private String cancellationPolicy;

    private String refundPolicy;

    private Boolean isFeatured;
}
