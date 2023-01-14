package com.digitedgy.piassist.service;

import com.digitedgy.piassist.entity.Feature;
import com.digitedgy.piassist.entity.Story;
import com.digitedgy.piassist.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class StoryService {

    @Autowired
    StoryRepository storyRepository;

    @Autowired
    FeatureService featureService;

    public Story save(Story story, String featureNo) {
        Optional<Feature> existingFeature = featureService.getFeatureById(featureNo);
        if(existingFeature.isPresent()) {
            story.setFeature(existingFeature.get());
            return storyRepository.save(story);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Feature does not exist!");
        }
    }

    public Story update(Story story) {
        Optional<Story> existingStory = storyRepository.findById(story.getId());
        if(existingStory.isPresent()) {
            story.setFeature(existingStory.get().getFeature());
            return storyRepository.save(story);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Story does not exist!");
        }
    }

    public void delete(Integer id) {
        Optional<Story> story = storyRepository.findById(id);
        if(story.isPresent()) {
            storyRepository.delete(story.get());
        }  else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Story does not exist!");
        }
    }
}
