package com.digitedgy.piassist.controller;

import com.digitedgy.piassist.entity.SupportActivity;
import com.digitedgy.piassist.service.SupportActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/support")
public class SupportActivityController {

    @Autowired
    SupportActivityService supportActivityService;

    @GetMapping("/{sprintName}")
    public ResponseEntity<?> getSupportActivitiesForSprint(@PathVariable String sprintName) {
        return new ResponseEntity<>(supportActivityService.getAllForSprint(sprintName), HttpStatus.OK);
    }

    @PostMapping("/{sprintName}")
    public ResponseEntity<?> saveSupportActivitiesForSprint(@RequestBody SupportActivity supportActivity, @PathVariable String sprintName) {
        return new ResponseEntity<>(supportActivityService.save(supportActivity,sprintName), HttpStatus.CREATED);
    }

    @PutMapping("/{sprintName}")
    public ResponseEntity<?> updateSupportActivitiesForSprint(@RequestBody SupportActivity supportActivity, @PathVariable String sprintName) {
        return new ResponseEntity<>(supportActivityService.save(supportActivity,sprintName), HttpStatus.OK);
    }


    @DeleteMapping("/{supportActivityId}")
    public ResponseEntity<?> deleteSupportActivity(@PathVariable Integer supportActivityId) {
        supportActivityService.delete(supportActivityId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
