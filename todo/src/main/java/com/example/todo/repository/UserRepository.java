package com.example.todo.repository;

import com.example.todo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //findById is not required as jpa already has it
    //this method is created to return user info by email, spring will automatically analyze that given is string and variable is email it return the user with matching email.
    Optional<User> findByEmail(String email);
}
