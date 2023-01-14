package com.digitedgy.piassist.controller;

import com.digitedgy.piassist.entity.PlanningPokerEstimate;
import com.digitedgy.piassist.entity.Sprint;
import com.digitedgy.piassist.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/sprint")
public class SprintController {

    @Autowired
    SprintService sprintService;

    @PostMapping(path = "/{piName}")
    public ResponseEntity<?> saveSprint(@RequestBody Sprint sprint, @PathVariable String piName) {
        return new ResponseEntity<>(sprintService.save(sprint,piName), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{piName}")
    public ResponseEntity<?> updateSprint(@RequestBody Sprint sprint, @PathVariable String piName) {
        return new ResponseEntity<>(sprintService.save(sprint,piName), HttpStatus.OK);
    }

    @GetMapping(path = "/{piName}")
    public ResponseEntity<?> getSprintsForPI(@PathVariable String piName) {
        return new ResponseEntity<>(sprintService.getAllSprintsForPI(piName), HttpStatus.OK);
    }
}
