package ru.clevertec.house.model.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResponseHouse(UUID uuid,
                            String area,
                            String country,
                            String city,
                            String street,
                            String number,
                            LocalDateTime createDate) {
}
