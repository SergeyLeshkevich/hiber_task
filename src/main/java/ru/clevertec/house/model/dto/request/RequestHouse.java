package ru.clevertec.house.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record RequestHouse(
        UUID uuid,
        @NotNull @Size(min = 2, max = 50, message = "Area should be between 2 and 50 characters")
        String area,
        @NotNull @Size(min = 2, max = 50, message = "Country should be between 2 and 50 characters")

        String country,
        @NotNull @Size(min = 2, max = 50, message = "City should be between 2 and 50 characters")
        String city,
        @NotNull @Size(min = 2, max = 50, message = "Street should be between 2 and 50 characters")
        String street,
        @NotNull @Size(min = 2, max = 50, message = "Number should be between 2 and 50 characters")
        String number) {
}
