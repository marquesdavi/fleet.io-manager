package com.api.manager.fleet.repository;

import com.api.manager.fleet.conf.AutoClosableSession;
import com.api.manager.fleet.domain.permission.Role;
import com.api.manager.fleet.exception.GenericException;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.query.SelectionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoleRepository {
    private final SessionFactory sessionFactory;
    private static final Logger logger = LoggerFactory.getLogger(RoleRepository.class);

    @Transactional(readOnly = true)
    public Optional<Role> findByName(String name) {
        try (AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession())) {
            SelectionQuery<Role> query = session.delegate().createSelectionQuery(
                    "FROM Role WHERE name = :name",
                    Role.class);
            query.setParameter("name", name);
            return Optional.ofNullable(query.uniqueResult());
        } catch (HibernateException e) {
            handleException("Error finding role by name", e);
            return Optional.empty();
        }
    }



    private void handleException(String message, HibernateException e) {
        logger.error(message + ": " + e.getMessage());
        throw new GenericException(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
