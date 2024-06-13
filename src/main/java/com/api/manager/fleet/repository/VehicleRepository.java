package com.api.manager.fleet.repository;

import com.api.manager.fleet.conf.AutoClosableSession;
import com.api.manager.fleet.domain.vehicle.Vehicle;
import com.api.manager.fleet.dto.response.DefaultPaginatedListDTO;
import com.api.manager.fleet.exception.GenericException;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.query.SelectionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VehicleRepository {
    private final SessionFactory sessionFactory;
    private static final Logger logger = LoggerFactory.getLogger(VehicleRepository.class);

    @Transactional(readOnly = true)
    public Optional<Vehicle> findById(Long id) {
        try (AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession())) {
            Vehicle vehicle = session.delegate().find(Vehicle.class, id);
            return Optional.ofNullable(vehicle);
        } catch (HibernateException e) {
            handleException("Error finding vehicle by ID", e);
            return Optional.empty();
        }
    }

    @Transactional
    public void save(Vehicle user) {
        sessionFactory.getCurrentSession().persist(user);
    }

    @Transactional(readOnly = true)
    public Optional<DefaultPaginatedListDTO<Vehicle>> findAll(Integer startRow, Integer maxResults) {
        try (AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession())) {
            String queryString = "SELECT vehicle FROM Vehicle as vehicle";
            SelectionQuery<Vehicle> query = session.delegate().createQuery(queryString, Vehicle.class);
            Query<Long> countQuery = session.delegate().createQuery("SELECT count(*) FROM Vehicle as vehicle", Long.class);

            query.setFirstResult(startRow);
            query.setMaxResults(maxResults);

            DefaultPaginatedListDTO<Vehicle> item = new DefaultPaginatedListDTO<>(
                    countQuery.uniqueResult(),
                    query.stream().toList()
            );
            return Optional.of(item);
        } catch (HibernateException e) {
            handleException("Error finding all vehicles", e);
            return Optional.empty();
        }
    }

    private void handleException(String message, HibernateException e) {
        logger.error(message + ": " + e.getMessage());
        throw new GenericException(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
