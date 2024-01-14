package ru.clevertec.house.service.util;

import ru.clevertec.house.model.House;
import ru.clevertec.house.model.Passport;
import ru.clevertec.house.model.Person;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class HouseTest implements TestBuilder {

    private int id = 1;
    private UUID personUuid = TestConstant.PERSON_UUID;
    private String name = TestConstant.NAME;
    private String surname = TestConstant.SURNAME;
    private Person.Sex sex = TestConstant.SEX;
    private LocalDateTime dateTime = TestConstant.DATE_TIME;
    private String passportSeries = TestConstant.PASSPORT_SERIES;
    private String passportNumber = TestConstant.PASSPORT_NUMBER;
    private UUID houseUuid = TestConstant.HOISE_UUID;
    private String area = TestConstant.AREA;
    private String country = TestConstant.COUNTRY;
    private String city = TestConstant.CITY;
    private String street = TestConstant.STREET;
    private String number = TestConstant.NUMBER;

    @Override
    public House build() {
        final var passport = new Passport(id, passportSeries, passportNumber);
        final var person = new Person(id, personUuid, name, surname, sex, passport, dateTime, dateTime, null, null);
        final var house = new House();
        house.setId(id);
        house.setUuid(houseUuid);
        house.setArea(area);
        house.setCountry(country);
        house.setCity(city);
        house.setStreet(street);
        house.setNumber(number);
        person.setHouse(house);
        house.setRegisteredPeople(Set.of(person));
        return house;
    }
}
