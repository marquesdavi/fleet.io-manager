package com.api.manager.fleet.repository;

import com.api.manager.fleet.conf.AutoClosableSession;
import com.api.manager.fleet.domain.customer.Customer;
import com.api.manager.fleet.util.exception.GenericException;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.SelectionQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {
    private final SessionFactory sessionFactory;

    @Transactional(readOnly = true)
    public Optional<Customer> findById(Long id) {
        AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession());
        try (session) {
            SelectionQuery<Customer> query = session.delegate()
                    .createSelectionQuery("SELECT customer FROM Customer as customer WHERE customer.id = :id", Customer.class);
            query.setParameter("id", id);

            return Optional.ofNullable(query.uniqueResult());
        } catch (HibernateException e) {
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
            session.persist(customer);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null){
                transaction.rollback();
            }
            throw new GenericException("Cannot persist the customer! Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
            session.close();
        }
    }

    @Transactional(readOnly = true)
    public Optional<List<Customer>> findAll(Integer startRow, Integer maxResults) {
        AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession());
        try (session) {
            StringBuilder queryString = new StringBuilder();
            queryString.append("SELECT customer FROM Customer as customer");
            queryString.append(" LEFT JOIN FETCH customer.vehicles");

            SelectionQuery<Customer> query = session.delegate()
                    .createQuery(queryString.toString(), Customer.class);

            query.setFirstResult(startRow);
            query.setMaxResults(maxResults);

            return Optional.of(query.stream().toList());
        } catch (HibernateException e) {
            throw new GenericException("The server could not complete the query!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public Long countFindAll() {
        AutoClosableSession session = new AutoClosableSession(sessionFactory.openSession());
        try (session) {
            Query query = session.delegate()
                    .createQuery("SELECT count(*) FROM Customer as customer");

            return ((Long) query.uniqueResult());
        } catch (HibernateException e) {
            throw new GenericException("The server could not complete the query!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
