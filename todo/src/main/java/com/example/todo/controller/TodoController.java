package com.example.todo.controller;

import org.springframework.security.core.Authentication;
import com.example.todo.service.TodoService;
import com.example.todo.models.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

    @Autowired
    TodoService todoservice;

    @GetMapping("/userTodo")
    ResponseEntity<List<Todo>> getUserTodos(Authentication authentication){
        String email = authentication.getName();
        return new ResponseEntity<>(todoservice.getTodoByUser(email), HttpStatus.OK);
    }

    //Path Variable - get the value entered int the url and give it to the method
    @GetMapping("/{id}")
    ResponseEntity<Todo> getById(@PathVariable Long id) {
        try{
            Todo getTodo = todoservice.getTodoByID(id);
            return new ResponseEntity<>(getTodo, HttpStatus.OK);
        }catch(RuntimeException e){
            Todo getTodo = null;
            return new ResponseEntity<>(getTodo, HttpStatus.NOT_FOUND);// if wrong id is given or empty table
        }
    }

    @GetMapping("/page")
    ResponseEntity<Page<Todo>> getTodoByPages(@RequestParam int pagenumber, @RequestParam int size ){
        return new ResponseEntity<>(todoservice.getTodoByPages(pagenumber, size), HttpStatus.OK);

    }

    @PostMapping("/create")
    //We gonna return http request so ResponseEntity is the class which is used to return a http request, in this case we gonna Todo type so we gave <Todo>
    ResponseEntity<?> createTodoForUser(@RequestBody Todo todo, Authentication authentication) {
        String email = authentication.getName();
        return new ResponseEntity<>(todoservice.createTodoForUser(todo, email), HttpStatus.CREATED);  //HttpStatus is a enum which has the status code
    }

    //this cant be called in chrome coz chrome url automatically take get mapping using path variable
    //other than get mapping like put and delete can be checked using post man app by changing the request type


    @PutMapping
    ResponseEntity<Todo> updateTodoById( @RequestBody Todo todo, Authentication authentication){
        String email = authentication.getName();
        return new ResponseEntity<>(todoservice.updateTodo(todo, email), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    void deleteTodoById(@PathVariable Long id, Authentication authentication){
        String email = authentication.getName();
        todoservice.deleteTodoById(id, email);
    }

    @DeleteMapping
    void deleteAllTodos(Authentication authentication){
        String email = authentication.getName();
        todoservice.deleteByUserEmail(email);
    }
}
