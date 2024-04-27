package com.api.manager.fleet.repository;

import com.api.manager.fleet.domain.vehicle.Vehicle;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class VehicleRepository {
    private SessionFactory sessionFactory;

    public VehicleRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public Vehicle findById(Long id) {
        return sessionFactory.getCurrentSession().get(Vehicle.class, id);
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
