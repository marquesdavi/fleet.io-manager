package com.expctrl.service.implementation;

import com.expctrl.dto.expense.ExpenseDTO;
import com.expctrl.service.IExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ExpenseServiceImplementation implements IExpenseService {
    /**
     * @param expenseDTO
     * @return
     */
    @Override
    public ResponseEntity<String> createExpense(ExpenseDTO expenseDTO) {
        return null;
    }

    /**
     * @param expenseDTO
     * @return
     */
    @Override
    public ResponseEntity<String> updateExpense(ExpenseDTO expenseDTO) {
        return null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<String> deleteExpense(Long id) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<ExpenseDTO> listExpense() {
        return null;
    }
}
