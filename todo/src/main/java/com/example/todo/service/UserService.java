package com.example.todo.service;

import com.example.todo.models.User;
import com.example.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public void createUser(User user){
        repository.save(user);
    }

    public User getById(Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));
    }

    public User getByEmail(String email){
        return repository.findByEmail(email).orElseThrow(() -> new RuntimeException("Wrong email!"));
    }
}
