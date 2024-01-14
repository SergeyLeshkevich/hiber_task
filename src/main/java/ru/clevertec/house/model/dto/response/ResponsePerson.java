package ru.clevertec.house.model.dto.response;

import ru.clevertec.house.model.Passport;
import ru.clevertec.house.model.Person;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResponsePerson(UUID uuid,
                             String name,
                             String surname,
                             Person.Sex sex,
                             Passport passport,
                             LocalDateTime createDate,
                             LocalDateTime updateDate) {
}
