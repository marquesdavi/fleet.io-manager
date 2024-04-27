package com.api.manager.fleet.repository;

import com.api.manager.fleet.domain.user.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return sessionFactory.getCurrentSession().get(User.class, id);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM User u JOIN FETCH u.role WHERE u.email = :email",
                        User.class
                )
                .setParameter("email", email)
                .uniqueResult();
    }


    @Transactional
    public void save(User user) {
        sessionFactory.getCurrentSession().persist(user);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from User", User.class).list();
    }
}
