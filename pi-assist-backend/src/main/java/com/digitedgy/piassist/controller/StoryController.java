package com.digitedgy.piassist.controller;

import com.digitedgy.piassist.entity.Sprint;
import com.digitedgy.piassist.entity.Story;
import com.digitedgy.piassist.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/story/")
public class StoryController {

    @Autowired
    StoryService storyService;

    @PostMapping(path = "/{featureNo}")
    public ResponseEntity<?> createNewStory(@RequestBody Story story, @PathVariable String featureNo) {
        return new ResponseEntity<>(storyService.save(story,featureNo), HttpStatus.CREATED);
    }

    @PutMapping(path = "/")
    public ResponseEntity<?> updateStory(@RequestBody Story story) {
        return new ResponseEntity<>(storyService.update(story), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{storyId}/")
    public ResponseEntity<?> saveSprint(@RequestBody Story story, @PathVariable Integer storyId) {
        storyService.delete(storyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
