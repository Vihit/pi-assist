package com.digitedgy.piassist.controller;

import com.digitedgy.piassist.entity.PI;
import com.digitedgy.piassist.service.PIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/pi")
public class PIController {

    @Autowired
    PIService piService;

    @PostMapping(path = "/")
    public ResponseEntity<?> savePI(@RequestBody PI pi) {
        return new ResponseEntity<>(piService.save(pi), HttpStatus.CREATED);
    }

    @PutMapping(path = "/")
    public ResponseEntity<?> updatePI(@RequestBody PI pi) {
        Optional<PI> savedPI = piService.findById(pi.getId());
        if(savedPI.isPresent()) {
            pi.setId(savedPI.get().getId());
            PI updatedPI = piService.save(pi);
            return new ResponseEntity<>(updatedPI, HttpStatus.CREATED);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PI does not exist!");
    }

    @GetMapping(path = "/")
    public ResponseEntity<?> getPIs() {
        return new ResponseEntity<>(piService.getAll(), HttpStatus.CREATED);
    }

    @GetMapping(path = "/open/")
    public ResponseEntity<?> getOpenPIs() {
        return new ResponseEntity<>(piService.getAllOpenPIs(), HttpStatus.CREATED);
    }
}
