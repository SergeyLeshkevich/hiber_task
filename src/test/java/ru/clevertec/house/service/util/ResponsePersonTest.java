package ru.clevertec.house.service.util;

import ru.clevertec.house.model.Passport;
import ru.clevertec.house.model.Person;
import ru.clevertec.house.model.dto.response.ResponsePerson;

import java.time.LocalDateTime;
import java.util.UUID;

public class ResponsePersonTest implements TestBuilder{
    private int id = TestConstant.ID;
    private UUID personUuid = TestConstant.PERSON_UUID;
    private String name = TestConstant.NAME;
    private String surname = TestConstant.SURNAME;
    private Person.Sex sex = TestConstant.SEX;
    private LocalDateTime dateTime = TestConstant.DATE_TIME;
    private String passportSeries = TestConstant.PASSPORT_SERIES;
    private String passportNumber = TestConstant.PASSPORT_NUMBER;

    @Override
    public ResponsePerson build() {
        final var person= new ResponsePerson(personUuid,
                name,
                surname,
                sex,
                new Passport(id, passportSeries, passportNumber),
                dateTime,
                dateTime);
        return  person;
    }
}
