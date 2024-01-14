package ru.clevertec.house.service;

import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.model.Person;
import ru.clevertec.house.model.dto.request.RequestPerson;
import ru.clevertec.house.model.dto.response.ResponsePerson;

import java.util.List;
import java.util.UUID;

public interface PersonService {
    ResponsePerson get(UUID uuid) throws NotFoundException;

    List<ResponsePerson> getAll(int pageSize, int numberPage);

    UUID create(RequestPerson personDto, UUID houseUuid);

    UUID update(UUID uuid, RequestPerson personDto) throws NotFoundException;

    void delete(UUID uuid);

    long countPeople();
}
