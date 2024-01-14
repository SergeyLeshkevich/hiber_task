package ru.clevertec.house.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.model.Passport;
import ru.clevertec.house.repository.PassportRepository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InPostgresPassportRepositoryImpl implements PassportRepository {

    private static final String SELECT_PASSPORT_BY_SERIES_AND_NUMBER = "SELECT * FROM passports WHERE passport_series=? AND passport_number=?";
    private static final String PASSPORT_SERIES = "passport_series";
    private static final String PASSPORT_NUMBER = "passport_number";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Passport> findBySeriesAndNumberPassport(String series, String number) {

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    SELECT_PASSPORT_BY_SERIES_AND_NUMBER,
                    new Object[]{series, number},
                    (resultSet, rowNum) -> {
                        Passport passport = new Passport();
                        passport.setPassportSeries(resultSet.getString(PASSPORT_SERIES));
                        passport.setPassportNumber(resultSet.getString(PASSPORT_NUMBER));
                        return passport;
                    })
            );
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Passport with series " + series + " and number " + number + " not found");
        }
    }
}
