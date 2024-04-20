package com.expctrl.service;

import com.expctrl.dto.expense.ExpenseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface IExpenseService {
    public ResponseEntity<String> createExpense(ExpenseDTO expenseDTO);
    public ResponseEntity<String> updateExpense(ExpenseDTO expenseDTO);
    public ResponseEntity<String> deleteExpense(Long id);
    public ResponseEntity<ExpenseDTO> listExpense();
}
