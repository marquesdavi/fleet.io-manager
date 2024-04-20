package com.expctrl.repository;

import com.expctrl.domain.permission.Role;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RoleRepository {
    SessionFactory sessionFactory;

    @Autowired
    public RoleRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public Role findByName(String name) {
        return sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM Role WHERE name = :name",
                        Role.class
                )
                .setParameter("name", name)
                .uniqueResult();
    }

}
