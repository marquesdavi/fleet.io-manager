package com.api.manager.fleet.repository;

import com.api.manager.fleet.conf.AutoClosableSession;
import com.api.manager.fleet.domain.customer.Customer;
import com.api.manager.fleet.domain.vehicle.Vehicle;
import com.api.manager.fleet.exception.GenericException;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VehicleRepository {
    private final SessionFactory sessionFactory;

    @Transactional(readOnly = true)
    public Vehicle findById(Long id) {
        try (AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession())) {
            Vehicle customer = session.delegate().find(Vehicle.class, id);
            return customer;
        } catch (HibernateException e) {
            throw new GenericException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void save(Vehicle user) {
        sessionFactory.getCurrentSession().persist(user);
    }

    @Transactional(readOnly = true)
    public List<Vehicle> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from User", Vehicle.class).list();
    }

}
