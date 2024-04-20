package com.expctrl.dto.expense;

public record ExpenseDTO(
        String title,
        String description,
        Double amount,
        String category,
        String date,
        String userId
) {
}
