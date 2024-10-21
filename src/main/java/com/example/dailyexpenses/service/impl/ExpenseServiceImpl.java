package com.example.dailyexpenses.service.impl;

import com.example.dailyexpenses.model.Expense;
import com.example.dailyexpenses.repository.ExpenseRepository;
import com.example.dailyexpenses.service.ExpenseService;
import com.example.dailyexpenses.utils.PdfGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public List<Expense> getExpensesByUserId(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    @Override
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @Override
    public Expense updateExpense(Long expenseId, Expense updatedExpense) {
        Expense existingExpense = expenseRepository.findById(expenseId).orElse(null);
        
        if (existingExpense != null) {
            existingExpense.setDescription(updatedExpense.getDescription());
            existingExpense.setAmount(updatedExpense.getAmount());
            return expenseRepository.save(existingExpense);
        } else {
            return null;
        }
    }

    @Override
    public ByteArrayOutputStream generateUserBalanceSheet(Long userId) throws Exception {
        List<Expense> expenses = getExpensesByUserId(userId);
        return PdfGenerator.createUserBalanceSheetPdf(expenses, userId);
    }

    @Override
    public ByteArrayOutputStream generateAllUsersBalanceSheet() throws Exception {
        List<Expense> allExpenses = getAllExpenses();
        return PdfGenerator.createAllUsersBalanceSheetPdf(allExpenses);
    }
}
