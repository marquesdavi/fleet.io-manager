package com.api.manager.fleet.repository;

import com.api.manager.fleet.conf.AutoClosableSession;
import com.api.manager.fleet.domain.user.User;
import com.api.manager.fleet.exception.GenericException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final SessionFactory sessionFactory;

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        try (AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession())) {
            User user = session.delegate().get(User.class, id);
            return Optional.ofNullable(user);
        } catch (HibernateException e) {
            handleException("Error finding user by ID", e);
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        log.info("Finding user with email: {}", email);
        try (AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession())) {
            SelectionQuery<User> query = session.delegate().createSelectionQuery(
                    "FROM User WHERE email = :email",
                    User.class);
            query.setParameter("email", email);
            User user = query.uniqueResult();
            if (user == null) {
                log.info("No user found with email: {}", email);
                return Optional.empty();
            }
            log.info("User found with email: {}", email);
            return Optional.of(user);
        } catch (HibernateException e) {
            log.error("Error finding user with email: {}", email, e);
            throw new GenericException("The server could not complete the query!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void save(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            log.info("Transaction started");
            session.persist(user);
            transaction.commit();
            log.info("Transaction finished successfully");
        } catch (HibernateException e) {
            if (transaction != null){
                transaction.rollback();
            }
            handleException("Error saving user", e);
        } finally {
            session.close();
        }
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        try (AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession())) {
            return session.delegate().createQuery("FROM User", User.class).list();
        } catch (HibernateException e) {
            handleException("Error finding all users", e);
            return List.of();
        }
    }

    private void handleException(String message, HibernateException e) {
        log.error(message + ": " + e.getMessage());
        throw new GenericException(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
