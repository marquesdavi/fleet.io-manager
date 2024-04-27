package com.api.manager.fleet.repository;

import com.api.manager.fleet.domain.client.Client;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ClientRepository {
    private SessionFactory sessionFactory;

    @Autowired
    public ClientRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return sessionFactory.getCurrentSession().get(Client.class, id);
    }

    @Transactional
    public void save(Client user) {
        sessionFactory.getCurrentSession().persist(user);
    }

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from Client", Client.class).list();
    }
}
