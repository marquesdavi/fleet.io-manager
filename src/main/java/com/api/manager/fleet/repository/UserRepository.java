package com.api.manager.fleet.repository;

import com.api.manager.fleet.conf.AutoClosableSession;
import com.api.manager.fleet.domain.customer.Customer;
import com.api.manager.fleet.domain.user.User;
import com.api.manager.fleet.util.exception.GenericException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.SelectionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public Optional<User> findByEmail(String email) {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        logger.info("Finding user with email: {}", email);

        try (AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession())) {
            SelectionQuery<User> query = session.delegate()
                    .createQuery("SELECT user FROM User as user WHERE user.email = :email", User.class);
            query.setParameter("email", email);

            User user = query.uniqueResult();
            if (user == null) {
                logger.info("No user found with email: {}", email);
                return Optional.empty();
            }
            logger.info("User found with email: {}", email);
            return Optional.of(user);
        } catch (HibernateException e) {
            logger.error("Error finding user with email: {}", email, e);
            throw new GenericException("The server could not complete the query!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
