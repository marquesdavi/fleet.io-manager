package com.api.manager.fleet.repository;

import com.api.manager.fleet.conf.AutoClosableSession;
import com.api.manager.fleet.domain.vehicle.Vehicle;
import com.api.manager.fleet.dto.response.DefaultPaginatedListDTO;
import com.api.manager.fleet.exception.GenericException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.SelectionQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class VehicleRepository {
    private final SessionFactory sessionFactory;

    @Transactional(readOnly = true)
    public Optional<Vehicle> findById(Long id) {
        try (AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession())) {
            Vehicle vehicle = session.delegate().find(Vehicle.class, id);
            return Optional.ofNullable(vehicle);
        } catch (HibernateException e) {
            throw handleException("Error finding vehicle by ID", e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Vehicle> findByPlateAndCustomerId(String plate, BigInteger customerId) {
        try (AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession())) {
            SelectionQuery<Vehicle> vehicle = session.delegate().createSelectionQuery(
                    "select vcl from Vehicle as vcl where vcl.plate = :plate and vcl.customer = :customer",
                    Vehicle.class);

            vehicle.setParameter("plate", plate);
            vehicle.setParameter("customer", customerId);

            return Optional.ofNullable(vehicle.uniqueResult());
        } catch (HibernateException e) {
            throw handleException("Error finding vehicle by ID and CustomerId", e);
        }
    }

    @Transactional
    public Vehicle save(Vehicle vehicle) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            log.info("Saving vehicle");
            session.save(vehicle);
            transaction.commit();
            log.info("Vehicle saved successfully");
            return vehicle;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw handleException("Error saving vehicle", e);
        } finally {
            session.close();
        }
    }

    @Transactional
    public void delete(Vehicle vehicle) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            log.info("Deleting vehicle");
            session.remove(vehicle);
            transaction.commit();
            log.info("Vehicle deleted successfully");
        } catch (HibernateException e) {
            if (transaction != null){
                transaction.rollback();
            }
            throw handleException("Error deleting vehicle", e);
        } finally {
            session.close();
        }
    }

    @Transactional(readOnly = true)
    public Optional<DefaultPaginatedListDTO<Vehicle>> findAll(Integer startRow, Integer maxResults, Optional<String> plate, Optional<BigInteger> customerId) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder("select v from Vehicle v");
            StringBuilder countQueryString = new StringBuilder("select count(v) from Vehicle v");

            boolean whereAdded = false;

            if (plate.isPresent()) {
                queryString.append(whereAdded ? " and" : " where").append(" v.plate = :plate");
                countQueryString.append(whereAdded ? " and" : " where").append(" v.plate = :plate");
                whereAdded = true;
            }

            if (customerId.isPresent()) {
                queryString.append(whereAdded ? " and" : " where").append(" v.customer = :customer");
                countQueryString.append(whereAdded ? " and" : " where").append(" v.customer = :customer");
            }

            Query<Vehicle> query = session.createQuery(queryString.toString(), Vehicle.class);
            Query<Long> countQuery = session.createQuery(countQueryString.toString(), Long.class);

            plate.ifPresent(p -> {
                query.setParameter("plate", p);
                countQuery.setParameter("plate", p);
            });

            customerId.ifPresent(id -> {
                query.setParameter("customer", id);
                countQuery.setParameter("customer", id);
            });

            query.setFirstResult(startRow);
            query.setMaxResults(maxResults);

            List<Vehicle> vehicles = query.getResultList();
            Long totalCount = countQuery.getSingleResult();

            DefaultPaginatedListDTO<Vehicle> item = new DefaultPaginatedListDTO<>(totalCount, vehicles);
            return Optional.of(item);
        } catch (HibernateException e) {
            throw handleException("Error finding all vehicles", e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Vehicle> findByPlate(String plate) {
        try (AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession())) {
            SelectionQuery<Vehicle> query = session.delegate().createSelectionQuery(
                    "select vcl from Vehicle as vcl where vcl.plate = :plate", Vehicle.class);
            query.setParameter("plate", plate);
            return Optional.ofNullable(query.uniqueResult());
        } catch (HibernateException e) {
            throw handleException("Error finding vehicle by PLATE", e);
        }
    }

    @Transactional
    public void update(Vehicle vehicle) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            log.info("Transaction started");
            session.merge(vehicle);
            transaction.commit();
            log.info("Transaction finished successfully");
        } catch (HibernateException e) {
            if (transaction != null){
                transaction.rollback();
            }
            throw handleException("Error updating vehicle", e);
        } finally {
            session.close();
        }
    }

    private GenericException handleException(String message, HibernateException e) {
        log.error(message + ": " + e.getMessage());
        return new GenericException(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
