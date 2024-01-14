package ru.clevertec.house.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import ru.clevertec.house.model.Passport;
import ru.clevertec.house.model.Person;

import java.util.UUID;

@Builder
public record RequestPerson(
        UUID uuid,
        @NotNull @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
        String name,
        @NotNull @Size(min = 2, max = 50, message = "Surname should be between 2 and 50 characters")
        String surname,
        Person.Sex sex,
        Passport passport) {
}
