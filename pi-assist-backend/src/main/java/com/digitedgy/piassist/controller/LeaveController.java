package com.digitedgy.piassist.controller;

import com.digitedgy.piassist.entity.Leave;
import com.digitedgy.piassist.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/leaves")
public class LeaveController {

    @Autowired
    LeaveService leaveService;

    @GetMapping(path = "/{user}")
    public ResponseEntity<?> getLeavesByUsername(@PathVariable String user) {
        return new ResponseEntity<>(leaveService.getLeavesByUsername(user), HttpStatus.OK);
    }

    @PostMapping(path = "/{user}")
    public ResponseEntity<?> saveLeavesForAUser(@PathVariable String user, @RequestBody Iterable<Leave> leaves) {
        return new ResponseEntity<>(leaveService.saveAll(leaves,user),HttpStatus.CREATED);
    }

    @PutMapping(path = "/{user}")
    public ResponseEntity<?> updateLeavesForAUser(@PathVariable String user, @RequestBody Iterable<Leave> leaves) {
        return new ResponseEntity<>(leaveService.update(leaves,user),HttpStatus.CREATED);
    }
}
