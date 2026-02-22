package com.example.todo.repository;

import com.example.todo.models.Todo;
import com.example.todo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

//CRUD - Create Read Update Delete
//JPARepository<T, I> is inbuilt interface used to perform CRUD operation.
//T denotes the object type and I denotes the id data type
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserEmail(String email);

    Optional<Todo> findByIdAndUserEmail(Long id, String email);

    void deleteByUserEmail(String email);
}
