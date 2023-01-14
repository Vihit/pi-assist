package com.digitedgy.piassist.service;

import com.digitedgy.piassist.entity.Feature;
import com.digitedgy.piassist.entity.PI;
import com.digitedgy.piassist.repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FeatureService {

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    PIService piService;

    public List<Feature> getAllFeatures() {
        return StreamSupport.stream(featureRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    public Iterable<Feature> getAllFeaturesForATeam(String team) {
        Optional<PI> pi = piService.findOpenPIForTeam(team);
        if(pi.isPresent()) {
            return featureRepository.findAllByTeamAndPi(team, pi.get().getId().toString());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An open PI does not exist!");
        }
    }

    public void saveAll(List<Feature> features) {
        features.stream().forEach(feature -> feature.getStories().forEach(story -> System.out.println(story.getId())));
        featureRepository.saveAll(features);
    }

    public Optional<Feature> getFeatureById(String featureNo) {
        return featureRepository.findById(featureNo);
    }

    public void deleteAll() {
        featureRepository.deleteAll();
    }

    public Feature update(Feature feature) {
        Optional<Feature> existingFeature = featureRepository.findById(feature.getId());
        if(existingFeature.isPresent()) {
            feature.setStories(existingFeature.get().getStories());
            return featureRepository.save(feature);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Feature does not exist!");
        }
    }

    public Feature save(Feature feature) {
        String init = Arrays.stream(feature.getTeam().split(" ")).map(f->f.charAt(0)+"").reduce((name1,name2)->name1+name2).get();
        int randomKey = (int)(Math.random()*100000);
        while(true) {
            if (!getFeatureById(init+"-"+randomKey).isPresent()) {
                feature.setId(init+"-"+randomKey);
                feature.setDiscussion("");
                feature.setEstimate(0);
                feature.setPlanningAssignee("");
                feature.setIsPlanned("N");
                return featureRepository.save(feature);
            } else {
                randomKey = (int)Math.random()*100000;
            }
        }
    }
}
