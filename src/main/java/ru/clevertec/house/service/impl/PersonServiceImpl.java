package ru.clevertec.house.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.exception.PassportValidException;
import ru.clevertec.house.mapper.PersonMapper;
import ru.clevertec.house.model.House;
import ru.clevertec.house.model.Passport;
import ru.clevertec.house.model.Person;
import ru.clevertec.house.model.dto.request.RequestPerson;
import ru.clevertec.house.model.dto.response.ResponsePerson;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.repository.PassportRepository;
import ru.clevertec.house.repository.PersonRepository;
import ru.clevertec.house.service.PersonService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final HouseRepository houseRepository;
    private final PassportRepository passportRepository;
    private final PersonMapper personMapper;

    @Override
    public ResponsePerson get(UUID uuid) throws NotFoundException {
        Optional<Person> person = personRepository.findByUuid(uuid);
        if (person.isEmpty()) {
            throw NotFoundException.of(Person.class, uuid);
        }
        return personMapper.toDto(person.get());
    }

    @Override
    public List<ResponsePerson> getAll(int pageSize, int numberPage) {
        return personMapper.toDtoList(personRepository.findAll(pageSize, numberPage));
    }

    @Override
    public UUID create(RequestPerson personDto, UUID houseUuid) throws NotFoundException, PassportValidException {
        Optional<House> house = houseRepository.findByUuid(houseUuid);

        if (house.isEmpty()) {
            throw NotFoundException.of(House.class, houseUuid);
        }
        if (!isValidPassport(personDto.passport())) {
            throw new PassportValidException("Passport with this number has already been saved");
        }
        Person person = personMapper.toEntity(personDto);
        person.setHouse(house.get());
        return personRepository.save(person);
    }

    @Override
    public UUID update(UUID uuid, RequestPerson personDto) throws NotFoundException {
        Optional<Person> personOptional = personRepository.findByUuid(uuid);
        if (personOptional.isEmpty()) {
            throw NotFoundException.of(Person.class, uuid);
        }

        Person person = personMapper.toEntity(personDto);
        Person personFromRepo = personOptional.get();

        if (person.equals(personFromRepo)) {
            return uuid;
        }

        personRepository.save(person);

        return uuid;
    }

    @Override
    public void delete(UUID uuid) {
        personRepository.delete(uuid);
    }

    @Override
    public long countPeople() {
        return personRepository.countPeople();
    }

    private boolean isValidPassport(Passport passport) {
        boolean isValid = false;
        if (passportRepository.findBySeriesAndNumberPassport(passport.getPassportSeries(),
                passport.getPassportNumber()).isEmpty()) {
            isValid = true;
        }
        return isValid;
    }
}
