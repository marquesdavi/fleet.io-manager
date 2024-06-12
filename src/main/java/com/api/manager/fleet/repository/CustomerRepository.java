package com.api.manager.fleet.repository;

import com.api.manager.fleet.conf.AutoClosableSession;
import com.api.manager.fleet.domain.customer.Customer;
import com.api.manager.fleet.dto.response.DefaultPaginatedListDTO;
import com.api.manager.fleet.exception.GenericException;
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

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {
    private final SessionFactory sessionFactory;
    private static final Logger logger = LoggerFactory.getLogger(CustomerRepository.class);

    @Transactional(readOnly = true)
    public Optional<Customer> findById(Long id) {
        try (AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession())) {
            Customer customer = session.delegate().find(Customer.class, id);
            return Optional.ofNullable(customer);
        } catch (HibernateException e) {
            handleException("Error finding customer by ID", e);
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    public Optional<Customer> findByCnpj(String cnpj) {
        try (AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession())) {
            SelectionQuery<Customer> query = session.delegate().createSelectionQuery(
                    "SELECT customer FROM Customer as customer WHERE customer.cnpj = :cnpj", Customer.class);
            query.setParameter("cnpj", cnpj);
            return Optional.ofNullable(query.uniqueResult());
        } catch (HibernateException e) {
            handleException("Error finding customer by CNPJ", e);
            return Optional.empty();
        }
    }

    @Transactional
    public void save(Customer customer) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            logger.info("Saving customer");
            session.persist(customer);
            transaction.commit();
            logger.info("Customer saved successfully");
        } catch (HibernateException e) {
            if (transaction != null){
                transaction.rollback();
            }
            handleException("Error saving customer", e);
        } finally {
            session.close();
        }
    }

    @Transactional(readOnly = true)
    public Optional<DefaultPaginatedListDTO<Customer>> findAll(Integer startRow, Integer maxResults) {
        try (AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession())) {
            String queryString = "SELECT customer FROM Customer as customer";
            SelectionQuery<Customer> query = session.delegate().createQuery(queryString, Customer.class);
            Query<Long> countQuery = session.delegate().createQuery("SELECT count(*) FROM Customer as customer", Long.class);

            query.setFirstResult(startRow);
            query.setMaxResults(maxResults);

            DefaultPaginatedListDTO<Customer> item = new DefaultPaginatedListDTO<>(
                    countQuery.uniqueResult(),
                    query.stream().toList()
            );
            return Optional.of(item);
        } catch (HibernateException e) {
            handleException("Error finding all customers", e);
            return Optional.empty();
        }
    }

    @Transactional
    public void delete(Customer customer) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            logger.info("Deleting customer");
            session.remove(customer);
            transaction.commit();
            logger.info("Customer deleted successfully");
        } catch (HibernateException e) {
            if (transaction != null){
                transaction.rollback();
            }
            handleException("Error deleting customer", e);
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
            if (transaction != null){
                transaction.rollback();
            }
            handleException("Error updating customer", e);
        } finally {
            session.close();
        }
    }

    private void handleException(String message, HibernateException e) {
        logger.error(message + ": " + e.getMessage());
        throw new GenericException(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
