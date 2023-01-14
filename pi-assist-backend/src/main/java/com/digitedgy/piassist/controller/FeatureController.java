package com.digitedgy.piassist.controller;

import com.digitedgy.piassist.entity.Feature;
import com.digitedgy.piassist.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/feature")
public class FeatureController {

    @Autowired
    FeatureService featureService;

    @PostMapping(path="/")
    public ResponseEntity<?> saveFeature(@RequestBody Feature feature) {
        return new ResponseEntity<>(featureService.save(feature),HttpStatus.OK);
    }

    @PutMapping(path = "/")
    public ResponseEntity<?> updateFeature(@RequestBody Feature feature) {
        Feature updatedFeature = featureService.update(feature);
        return new ResponseEntity<>(updatedFeature, HttpStatus.OK);
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<?> getFeature(@PathVariable String id) {
        return new ResponseEntity<>(featureService.getFeatureById(id),HttpStatus.OK);
    }

    @GetMapping(path = "/all/")
    public ResponseEntity<?> getAllFeatures() {
        return new ResponseEntity<>(featureService.getAllFeatures(),HttpStatus.OK);
    }

    @GetMapping(path = "/all/{team}")
    public ResponseEntity<?> getAllFeaturesForATeam(@PathVariable String team) {
        return new ResponseEntity<>(featureService.getAllFeaturesForATeam(team),HttpStatus.OK);
    }
}
