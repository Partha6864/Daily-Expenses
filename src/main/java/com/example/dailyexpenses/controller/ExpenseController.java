package com.example.dailyexpenses.controller;

import com.example.dailyexpenses.model.Expense;
import com.example.dailyexpenses.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/add")
    public ResponseEntity<Object> addExpense(@RequestBody Expense expense) {
        try {
            Expense createdExpense = expenseService.addExpense(expense);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdExpense);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to add expense: " + e.getMessage());
        }
    }

    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<List<Expense>> getExpensesByUserId(@PathVariable Long userId) {
        try {
            List<Expense> expenses = expenseService.getExpensesByUserId(userId);
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Expense>> getAllExpenses() {
        try {
            List<Expense> expenses = expenseService.getAllExpenses();
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping("/update/{expenseId}")
    public ResponseEntity<Object> updateExpense(@PathVariable Long expenseId, @RequestBody Expense updatedExpense) {
        try {
            Expense expense = expenseService.updateExpense(expenseId, updatedExpense);
            if (expense != null) {
                return ResponseEntity.ok(expense);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Expense not found with ID: " + expenseId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update expense: " + e.getMessage());
        }
    }

    @GetMapping("/balance-sheet/user/{userId}")
    public ResponseEntity<InputStreamResource> downloadUserBalanceSheet(@PathVariable Long userId) {
        try {
            ByteArrayOutputStream outputStream = expenseService.generateUserBalanceSheet(userId);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=balance_sheet_user_" + userId + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/balance-sheet/all")
    public ResponseEntity<InputStreamResource> downloadAllUsersBalanceSheet() {
        try {
            ByteArrayOutputStream outputStream = expenseService.generateAllUsersBalanceSheet();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=balance_sheet_all_users.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
