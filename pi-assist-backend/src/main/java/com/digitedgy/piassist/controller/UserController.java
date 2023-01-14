package com.digitedgy.piassist.controller;

import com.digitedgy.piassist.entity.User;
import com.digitedgy.piassist.service.FeatureService;
import com.digitedgy.piassist.service.JiraService;
import com.digitedgy.piassist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JiraService jiraService;

    @Autowired
    FeatureService featureService;

    @GetMapping(path = "/users")
    public ResponseEntity<?> getUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping(path = "/users/{team}")
    public ResponseEntity<?> getUsersByTeam(@PathVariable String team) {
        return new ResponseEntity<>(userService.findAllByTeam(team),HttpStatus.OK);
    }

    @GetMapping(path = "/user/{id}")
    public ResponseEntity<?> getUsers(@PathVariable String id) {
        return new ResponseEntity<>(userService.getUser(Long.parseLong(id)),HttpStatus.OK);
    }

    @PostMapping(path = "/user/")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        user.setPwd(bCryptPasswordEncoder.encode(user.getPwd()));
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser,HttpStatus.OK);
    }

    @PutMapping(path = "/user/")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Optional<User> existingUser = userService.getUserByUsername(user.getUsername());
        if(existingUser.isPresent()) {
            user.setPwd(existingUser.get().getPwd());
            User savedUser = userService.saveUser(user);
            return new ResponseEntity<>(savedUser,HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"User does not exist!");
        }
    }
}
