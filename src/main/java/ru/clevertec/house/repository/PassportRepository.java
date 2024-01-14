package ru.clevertec.house.repository;

import ru.clevertec.house.model.Passport;

import java.util.Optional;

public interface PassportRepository {
    Optional<Passport> findBySeriesAndNumberPassport(String series, String number);
}
