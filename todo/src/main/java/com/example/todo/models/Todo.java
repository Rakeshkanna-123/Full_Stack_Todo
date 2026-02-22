package com.example.todo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.MemoryAddress;
import lombok.Data;


@Entity//denotes that this class is a table (JPA will automatically creates it)
@Data//enables getters and setters method automatically for all the parameters inside this class
//without @Data the values won't be inserted or return, jpa wont do this automatically , so @Data provides setters and getters and helps to return and insert the parameters
public class Todo {

    @Id
    @GeneratedValue
    //long -> primitive data type, Long -> class. Long is used because JPARepository is a interface which accepts non-primitive data types <T, I>
    Long id;

    @NotNull
    @NotBlank//ensures user entered title without leaving it empty
    String title;
    String description;
    boolean completed;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
}
