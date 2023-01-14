package com.digitedgy.piassist.controller;

import com.digitedgy.piassist.entity.Feature;
import com.digitedgy.piassist.entity.User;
import com.digitedgy.piassist.model.LoggedInUser;
import com.digitedgy.piassist.service.JiraService;
import com.digitedgy.piassist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class MainController {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    JiraService jiraService;

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        Optional<User> existingUser = userService.getUserByUsername(user.getUsername());
        if(existingUser.isPresent()) {
            User userInDb = existingUser.get();
            System.out.println(bCryptPasswordEncoder.encode(user.getPwd()));
            if(bCryptPasswordEncoder.matches(user.getPwd(), userInDb.getPwd())) {
                LoggedInUser loggedInUser = new LoggedInUser(userInDb.getUsername(), userInDb.getName(), userInDb.getRole(),
                        userInDb.getTeam(), user.getPwd(), user.getCapacity());
                return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Username or password is incorrect!");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"User does not exist!");
        }
    }

    @PostMapping(path = "/refresh/{team}")
    public ResponseEntity<?> refreshJiras(@RequestHeader("Authorization") String authHeader, @PathVariable String team) {
        jiraService.refreshJiras(authHeader, team);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/refreshSprints/{team}")
    public ResponseEntity<?> refreshSprints(@RequestHeader("Authorization") String authHeader, @PathVariable String team) {
        jiraService.refreshPISprint(authHeader, team);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/pushToJira/")
    public ResponseEntity<?> pushFeature(@RequestHeader("Authorization") String authHeader, @RequestBody Feature feature) {
        jiraService.pushFeatureToJira(authHeader, feature);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
