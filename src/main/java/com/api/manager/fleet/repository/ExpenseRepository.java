package com.expctrl.repository;

import com.expctrl.domain.expense.Expense;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ExpenseRepository {
    private SessionFactory sessionFactory;

    @Autowired
    public ExpenseRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Expense findById(Long id) {
        return sessionFactory.getCurrentSession().get(Expense.class, id);
    }

    @Transactional
    public void update(Expense exp) {
        sessionFactory
                .getCurrentSession()
                .createMutationQuery(
                        "UPDATE Expense SET :field = :value WHERE :field = :search"
                );
    }

    @Transactional
    public void delete(Expense exp) {
        sessionFactory
                .getCurrentSession().remove(exp);
    }

    @Transactional
    public void createExpense(Expense exp) {
        sessionFactory
                .getCurrentSession().persist(exp);
    }

    @Transactional(readOnly = true)
    public List<Expense> findAll() {
        return sessionFactory
                .getCurrentSession()
                .createQuery("from Expense exp JOIN FETCH exp.payments", Expense.class).list();
    }

    public Expense findByStatus(Boolean settled) {
        return sessionFactory
               .getCurrentSession()
               .createQuery("from Expense exp WHERE exp.settled = :settled", Expense.class)
               .setParameter("settled", settled)
               .uniqueResult();
    }
}
