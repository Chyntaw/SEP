package com.gruppe_f.sep.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/user/findall")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = service.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/user/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User newUser = service.addUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

}