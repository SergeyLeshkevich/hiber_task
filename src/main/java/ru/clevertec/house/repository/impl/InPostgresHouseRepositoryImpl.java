package ru.clevertec.house.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.model.House;
import ru.clevertec.house.repository.HouseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InPostgresHouseRepositoryImpl implements HouseRepository {

    private static final String JPQL_SELECT_BY_UUID = "FROM House h WHERE h.uuid= :uuid";
    private static final String JPQL_SELECT_COUNT_HOUSES = "Select count(h.id) from House h";
    private static final String JPQL_FROM_HOUSES = "From House";
    private static final String UUID = "uuid";

    private final EntityManagerFactory factory;

    @Override
    public Optional<House> findByUuid(UUID uuid) {

        try(EntityManager entityManager = factory.createEntityManager()) {
            TypedQuery<House> query = entityManager.createQuery(JPQL_SELECT_BY_UUID, House.class);
            query.setParameter(UUID, uuid);
            return Optional.of(query.getSingleResult());

        } catch (NoResultException e) {
            throw NotFoundException.of(House.class, uuid);
        }
    }

    @Override
    public UUID save(House house) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        UUID uuid = java.util.UUID.randomUUID();

        try {
            transaction.begin();

            if (house.getUuid() == null) {
                house.setUuid(uuid);
                entityManager.persist(house);
            } else {
                house = entityManager.merge(house);
            }

            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            entityManager.close();
        }

        return house.getUuid();
    }

    @Override
    public void delete(UUID uuid) {

        try (EntityManager entityManager = factory.createEntityManager()) {
            TypedQuery<House> query = entityManager.createQuery(JPQL_SELECT_BY_UUID, House.class);
            query.setParameter(UUID, uuid);
            House houseToDelete = query.getSingleResult();

            entityManager.remove(houseToDelete);
        }
    }

    @Override
    public List<House> findAll(int pageSize, int numberPage) {

        try (EntityManager entityManager = factory.createEntityManager()) {
            Query query = entityManager.createQuery(JPQL_FROM_HOUSES);
            query.setFirstResult((numberPage - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        }
    }

    @Override
    public long countPeople() {

        try (EntityManager entityManager = factory.createEntityManager()) {
            Query query = entityManager.createQuery(JPQL_SELECT_COUNT_HOUSES);
            return (Long) query.getSingleResult();
        }
    }
}
