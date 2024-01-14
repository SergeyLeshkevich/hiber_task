package ru.clevertec.house.repository;

import org.springframework.stereotype.Repository;
import ru.clevertec.house.model.Person;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface PersonRepository {

    UUID save(Person person);

    void delete(UUID uuid);

    List<Person> findAll(int pageSize, int numberPage);

    Optional<Person> findByUuid(UUID uuid);

    List<Person> findByUuid(Set<UUID> registeredPeople);

    long countPeople();
}
