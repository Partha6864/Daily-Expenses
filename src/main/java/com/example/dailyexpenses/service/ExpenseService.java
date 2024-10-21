package com.example.dailyexpenses.service;

import com.example.dailyexpenses.model.Expense;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface ExpenseService {
    Expense addExpense(Expense expense);
    List<Expense> getExpensesByUserId(Long userId);
    List<Expense> getAllExpenses();
    Expense updateExpense(Long expenseId, Expense updatedExpense);
    ByteArrayOutputStream generateUserBalanceSheet(Long userId) throws Exception;
    ByteArrayOutputStream generateAllUsersBalanceSheet() throws Exception;
    void splitExpense(Expense expense);
}
