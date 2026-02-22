package com.example.todo.service;

import org.springframework.security.core.Authentication;
import com.example.todo.models.Todo;
import com.example.todo.models.User;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private UserRepository userRepository;


    //todo is a object belongs to Todo class(which contains the  column of the TODO table)
    //save() is a inbuilt method in the JPARespository interface used to add a new row or update the already existing row .
    public Todo createTodoForUser(Todo todo, String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not Found"));
        todo.setUser(user);
        return todoRepository.save(todo);
    }

    public Todo getTodoByID(Long id){
        return todoRepository.findById(id).orElseThrow(() -> new RuntimeException("ToDo not found"));
    }

    public Page<Todo> getTodoByPages(int pagenumber, int size){
        Pageable pageable = PageRequest.of(pagenumber, size);
        return todoRepository.findAll(pageable);
    }

    public List<Todo> getTodoByUser(String email){
        return todoRepository.findByUserEmail(email);
    }

    public Todo updateTodo(Todo todo, String email){
        Todo existingTodo = todoRepository.findByIdAndUserEmail(todo.getId(), email)
                .orElseThrow(() -> new RuntimeException("Not Allowed"));
        existingTodo.setTitle(todo.getTitle());
        existingTodo.setDescription(todo.getDescription());
        existingTodo.setCompleted(todo.isCompleted());
        return todoRepository.save(existingTodo);
    }

    //delete by id
    public void deleteTodoById(Long id, String email){
        Todo todo = todoRepository.findByIdAndUserEmail(id, email).orElseThrow(() -> new RuntimeException("Not Allowed"));
        todoRepository.delete(todo); //first get the todo by id using getTodoById method
    }

    //user needs to enter all the credentials for the todo
    @Transactional
    public void deleteByUserEmail(String email){
        todoRepository.deleteByUserEmail(email);
    }
}
