package ru.clevertec.house.service;

import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.model.dto.request.RequestHouse;
import ru.clevertec.house.model.dto.response.ResponseHouse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface HouseService {

    ResponseHouse get(UUID uuid) throws NotFoundException;

    List<ResponseHouse> getAll(int pageSize, int numberPage);

    UUID create(RequestHouse houseDto);

    UUID update(UUID uuid, RequestHouse houseDto, Set<UUID> ownersUuid) throws NotFoundException;

    void delete(UUID uuid);
    long countPeople();

}
