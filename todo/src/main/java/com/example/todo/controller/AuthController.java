package com.example.todo.controller;

import com.example.todo.Utils.JwtUtil;

import com.example.todo.dto.SignUpRequest;
import com.example.todo.models.User;
import com.example.todo.repository.UserRepository;
import com.example.todo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/auth")
@AllArgsConstructor // it takes care of the object creation like @Autowired, here iam gonna declare more objects so i use this.
public class AuthController {

    private UserRepository repository;
    private UserService service;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    @PostMapping("/sign_up")
    public ResponseEntity<String> signUpUser(@RequestBody SignUpRequest request){
        String email = request.getEmail();
        String password = passwordEncoder.encode(request.getPassword());

        if(repository.findByEmail(email).isPresent()){
            return new ResponseEntity<>("Email already exist!", HttpStatus.CONFLICT);
        }
        service.createUser(User.builder().email(email).password(password).build());
        return new ResponseEntity<>("Successfully Signed up!", HttpStatus.CREATED);
    }


    // ?  this means this method can return any kind of data type
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> body){
        String email = body.get("email");
        String password = body.get("password");

        var userOptional = repository.findByEmail(email);
        if(userOptional.isEmpty()){
            return new ResponseEntity<>("User not signed up!",HttpStatus.UNAUTHORIZED);
        }
        User user = userOptional.get();
        if(!passwordEncoder.matches(password, user.getPassword())){
            return new ResponseEntity<>("Invalid User!", HttpStatus.UNAUTHORIZED);
        }
        String token = jwtUtil.generateToken(email);
        return ResponseEntity.ok(Map.of("token",token));

    }

}
