package com.example.todo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor//denotes acceppt the row with all values
@NoArgsConstructor//denotes accpet the row even with missing values
@Builder// it ceated the user without any need for manual declaration of constructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Email
    private String email;

    private String password;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Todo> todos;



}
