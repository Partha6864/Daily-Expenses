package com.example.dailyexpenses.repository;


import com.example.dailyexpenses.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
