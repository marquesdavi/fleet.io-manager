package com.api.manager.fleet.repository;

import com.api.manager.fleet.conf.AutoClosableSession;
import com.api.manager.fleet.domain.customer.Customer;
import com.api.manager.fleet.dto.response.DefaultPaginatedListDTO;
import com.api.manager.fleet.service.implementation.CustomerService;
import com.api.manager.fleet.util.exception.GenericException;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
public class CustomerRepository {
    private final SessionFactory sessionFactory;

    private static final Logger logger = LoggerFactory.getLogger(CustomerRepository.class);

    @Transactional(readOnly = true)
    public Optional<Customer> findById(Long id) {
        AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession());
        try (session) {
            Customer query = session.delegate()
                    .find(Customer.class, id);

            return Optional.ofNullable(query);
        } catch (HibernateException e) {
            logger.error("The server could not complete the query!");
            throw new GenericException("The server could not complete the query!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Customer> findByCnpj(String cnpj) {
        AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession());
        try (session) {
            SelectionQuery<Customer> query = session.delegate()
                    .createSelectionQuery("SELECT customer FROM Customer as customer WHERE customer.cnpj = :cnpj", Customer.class);
            query.setParameter("cnpj", cnpj);

            return Optional.ofNullable(query.uniqueResult());
        } catch (HibernateException e) {
            throw new GenericException("The server could not complete the query!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void save(Customer customer) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            logger.info("Transaction started");
            session.persist(customer);
            transaction.commit();
            logger.info("Transaction finished successfully");
        } catch (HibernateException e) {
            if (transaction != null){
                transaction.rollback();
            }
            logger.error("Transaction failed " + e.getMessage());
            throw new GenericException("Cannot persist the customer! Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
            session.close();
        }
    }

    @Transactional(readOnly = true)
    public Optional<DefaultPaginatedListDTO<Customer>> findAll(Integer startRow, Integer maxResults) {
        var session = new AutoClosableSession(sessionFactory.openSession());
        try (session) {
            String queryString = "SELECT customer FROM Customer as customer";
            SelectionQuery<Customer> query = session.delegate().createQuery(queryString, Customer.class);

            Query<Long> countQuery = session.delegate()
                    .createQuery("SELECT count(*) FROM Customer as customer", Long.class);

            query.setFirstResult(startRow);
            query.setMaxResults(maxResults);

            DefaultPaginatedListDTO<Customer> item = new DefaultPaginatedListDTO<>(
                    countQuery.uniqueResult(),
                    query.stream().toList()
            );

            return Optional.of(item);
        } catch (HibernateException e) {
            logger.error("The server could not complete the query: " + e.getMessage());
            throw new GenericException("The server could not complete the query!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void delete(Customer customer) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            logger.info("Transaction started");
            session.remove(customer);
            transaction.commit();
            logger.info("Transaction finished successfully");
        } catch (HibernateException e) {
            logger.error("Transaction failed " + e.getMessage());
            throw new GenericException("The server could not complete the query!", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            session.close();
        }
    }

    @Transactional
    public void update(Customer customer) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            logger.info("Transaction started");
            session.merge(customer);
            transaction.commit();
            logger.info("Transaction finished successfully");
        } catch (HibernateException e) {
            logger.error("Transaction failed " + e.getMessage());
            throw new GenericException("The server could not complete the query!", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            session.close();
        }
    }
}
