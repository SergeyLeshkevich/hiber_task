package ru.clevertec.house.repository;

import org.springframework.stereotype.Repository;
import ru.clevertec.house.model.House;
import ru.clevertec.house.model.Person;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface HouseRepository  {

    Optional<House> findByUuid(UUID uuid);
    UUID save(House house);

    void delete(UUID uuid);

    List<House> findAll(int pageSize, int numberPage);

    long countPeople();
}
