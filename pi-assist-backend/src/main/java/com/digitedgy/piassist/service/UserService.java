package com.digitedgy.piassist.service;

import com.digitedgy.piassist.entity.User;
import com.digitedgy.piassist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Iterable<User> findAllByTeam(String team) {
        return userRepository.findAllByTeam(team);
    }
}
