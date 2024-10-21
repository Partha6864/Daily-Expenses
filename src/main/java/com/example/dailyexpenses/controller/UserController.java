package com.example.dailyexpenses.controller;

import com.example.dailyexpenses.model.User;
import com.example.dailyexpenses.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create user: " + e.getMessage());
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching user: " + e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching all users: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User existingUser = userService.getUserById(id);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found.");
            }
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update user: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable Long id) {
        try {
            boolean isDeleted = userService.deleteUserById(id);
            if (isDeleted) {
                return ResponseEntity.ok("User deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting user: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Object> deleteAllUsers() {
        try {
            userService.deleteAllUsers();
            return ResponseEntity.ok("All users deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting all users: " + e.getMessage());
        }
    }
}
