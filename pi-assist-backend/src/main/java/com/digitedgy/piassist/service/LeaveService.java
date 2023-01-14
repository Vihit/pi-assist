package com.digitedgy.piassist.service;

import com.digitedgy.piassist.entity.Leave;
import com.digitedgy.piassist.entity.User;
import com.digitedgy.piassist.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class LeaveService {

    @Autowired
    LeaveRepository leaveRepository;

    @Autowired
    UserService userService;

    public Iterable<Leave> saveAll(Iterable<Leave> leaves, String user) {
        return update(leaves,user);
    }

    @Transactional
    public Iterable<Leave> update(Iterable<Leave> leaves, String user) {
        Optional<User> existingUser = userService.getUserByUsername(user);
        if(existingUser.isPresent()) {
            Iterable<Leave> existingLeaves = existingUser.get().getLeaves();
            existingUser.get().setLeaves(null);
            leaveRepository.deleteAll(existingLeaves);
            leaves.forEach(leave -> leave.setUser(existingUser.get()));
            return leaveRepository.saveAll(leaves);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist!");
        }
    }

    public Iterable<Leave> getLeavesByUsername(String user) {
        Optional<User> existingUser = userService.getUserByUsername(user);
        if(existingUser.isPresent()) {
            Iterable<Leave> existingLeaves = existingUser.get().getLeaves();
            return existingLeaves;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist!");
        }
    }
}
