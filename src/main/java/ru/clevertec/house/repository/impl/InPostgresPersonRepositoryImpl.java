package ru.clevertec.house.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.model.Person;
import ru.clevertec.house.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
@Slf4j
@RequiredArgsConstructor
public class InPostgresPersonRepositoryImpl implements PersonRepository {
    private static final String JPQL_SELECT_BY_UUID = "FROM Person p WHERE p.uuid= :uuid";
    private static final String JPQL_SELECT_PEOPLE_BY_UUID = "SELECT h FROM Person h WHERE h.uuid IN :uuid";
    private static final String JPQL_SELECT_COUNT_PEOPLE = "Select count(p.id) from Person p";
    private static final String JPQL_FROM_PERSON = "From Person";

    private final EntityManagerFactory factory;

    @Override
    public UUID save(Person person) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        UUID uuid = UUID.randomUUID();
        try {
            transaction.begin();
            if (person.getUuid() == null) {
                person.setUuid(uuid);
                entityManager.persist(person);
            } else {
               person = entityManager.merge(person);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            log.error(e.getMessage());
        } finally {
            entityManager.close();
        }
        return person.getUuid();
    }


    @Override
    public void delete(UUID uuid) {
        EntityManager entityManager = factory.createEntityManager();

        try {

            TypedQuery<Person> query = entityManager.createQuery(JPQL_SELECT_BY_UUID, Person.class);
            query.setParameter("uuid", uuid);
            Person personToDelete = query.getSingleResult();

            entityManager.remove(personToDelete);

        } catch (NoResultException e) {
            throw NotFoundException.of(Person.class, uuid);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Person> findAll(int pageSize, int numberPage) {
        EntityManager entityManager = factory.createEntityManager();

        try {
            Query query = entityManager.createQuery(JPQL_FROM_PERSON);

            query.setFirstResult((numberPage - 1) * pageSize);
            query.setMaxResults(pageSize);

            return query.getResultList();
        } finally {
            entityManager.close();
        }

    }

    @Override
    public Optional<Person> findByUuid(UUID uuid) {

        EntityManager entityManager = factory.createEntityManager();

        try {
            TypedQuery<Person> query = entityManager.createQuery(JPQL_SELECT_BY_UUID, Person.class);
            query.setParameter("uuid", uuid);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            throw NotFoundException.of(Person.class, uuid);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Person> findByUuid(Set<UUID> registeredPeople) {

        EntityManager entityManager = factory.createEntityManager();

        try {
            return entityManager.createQuery(JPQL_SELECT_PEOPLE_BY_UUID, Person.class)
                    .setParameter("uuid", registeredPeople)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public long countPeople() {
        EntityManager entityManager = factory.createEntityManager();
        try {
            Query query = entityManager.createQuery(JPQL_SELECT_COUNT_PEOPLE);
            return (Long) query.getSingleResult();
        } finally {
            entityManager.close();
        }

    }
}
