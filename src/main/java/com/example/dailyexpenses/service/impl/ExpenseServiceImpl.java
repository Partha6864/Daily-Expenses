package com.example.dailyexpenses.service.impl;

import com.example.dailyexpenses.model.Expense;
import com.example.dailyexpenses.model.ExpenseParticipant;
import com.example.dailyexpenses.repository.ExpenseRepository;
import com.example.dailyexpenses.service.ExpenseService;
import com.example.dailyexpenses.utils.PdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public Expense addExpense(Expense expense) {
        splitExpense(expense);
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
            existingExpense.setSplitType(updatedExpense.getSplitType());
            existingExpense.setParticipants(updatedExpense.getParticipants());
            splitExpense(existingExpense);
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

    @Override
    public void splitExpense(Expense expense) {
        List<ExpenseParticipant> participants = expense.getParticipants();

        switch (expense.getSplitType()) {
            case EQUAL:
                BigDecimal equalShare = expense.getAmount().divide(BigDecimal.valueOf(participants.size()));
                participants.forEach(participant -> participant.setAmount(equalShare));
                break;

            case EXACT:
                BigDecimal totalAmount = participants.stream()
                        .map(ExpenseParticipant::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                if (totalAmount.compareTo(expense.getAmount()) != 0) {
                    throw new IllegalArgumentException("The specified amounts do not add up to the total expense.");
                }
                break;

            case PERCENTAGE:
                double totalPercentage = participants.stream()
                        .mapToDouble(ExpenseParticipant::getPercentage)
                        .sum();
                if (totalPercentage != 100) {
                    throw new IllegalArgumentException("The percentages must add up to 100%.");
                }
                participants.forEach(participant -> {
                    BigDecimal participantAmount = expense.getAmount()
                            .multiply(BigDecimal.valueOf(participant.getPercentage() / 100));
                    participant.setAmount(participantAmount);
                });
                break;
        }
    }
}
