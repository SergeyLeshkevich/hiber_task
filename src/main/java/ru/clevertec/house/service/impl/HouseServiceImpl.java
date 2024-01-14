package ru.clevertec.house.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.mapper.HouseMapper;
import ru.clevertec.house.model.Person;
import ru.clevertec.house.model.dto.request.RequestHouse;
import ru.clevertec.house.model.dto.response.ResponseHouse;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.repository.PersonRepository;
import ru.clevertec.house.service.HouseService;
import ru.clevertec.house.model.House;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final PersonRepository personRepository;
    private final HouseMapper houseMapper;

    @Override
    public ResponseHouse get(UUID uuid) throws NotFoundException {
        Optional<House> house = houseRepository.findByUuid(uuid);

        if (house.isEmpty()) {
            throw NotFoundException.of(House.class, uuid);
        }
        return houseMapper.toDto(house.get());
    }

    @Override
    public List<ResponseHouse> getAll(int pageSize, int numberPage) {
        return houseMapper.toDtoList(houseRepository.findAll(pageSize, numberPage));
    }

    @Override
    public UUID create(RequestHouse houseDto) {
        House house = houseMapper.toEntity(houseDto);
        return houseRepository.save(house);
    }

    @Override
    public UUID update(UUID uuid, RequestHouse houseDto, Set<UUID> registeredPeople) throws NotFoundException {
        checkInputSetPeopleForAvailability(registeredPeople);
        House house = houseMapper.toEntity(houseDto);
        houseRepository.save(house);
        return uuid;
    }

    @Override
    public void delete(UUID uuid) {
        houseRepository.delete(uuid);
    }

    @Override
    public long countPeople() {
       return houseRepository.countPeople();
    }


    private void checkInputSetPeopleForAvailability(Set<UUID> registeredPeople) {
        List<Person> personFromRepository = personRepository.findByUuid(registeredPeople);
        if (personFromRepository.size() != registeredPeople.size()) {
            List<UUID> inputUuidList = registeredPeople.stream().sorted().toList();
            List<UUID> personFromRepositoryUuid = personFromRepository.stream().map(Person::getUuid).sorted().toList();
            for (int i = 0; i < inputUuidList.size(); i++) {
                if (i < personFromRepositoryUuid.size()) {
                    if (!inputUuidList.get(i).equals(personFromRepositoryUuid.get(i))) {
                        throw NotFoundException.of(Person.class, inputUuidList.get(i));
                    }
                } else {
                    throw NotFoundException.of(Person.class, inputUuidList.get(i));
                }
            }
        }
    }
}
