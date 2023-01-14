package com.digitedgy.piassist.service;

import com.digitedgy.piassist.entity.PI;
import com.digitedgy.piassist.entity.Sprint;
import com.digitedgy.piassist.repository.SprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SprintService {

    @Autowired
    SprintRepository sprintRepository;

    @Autowired
    PIService piService;

    public Iterable<Sprint> getAllSprintsForPI(String pi) {
        Optional<PI> existingPI = piService.getPIByName(pi);
        if(existingPI.isPresent()){
            return sprintRepository.findAllByPiId(existingPI.get().getId());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PI does not exist!");
        }
    }

    public Sprint save(Sprint sprint, String pi) {
        Optional<PI> existingPI = piService.getPIByName(pi);
        if(existingPI.isPresent()){
            sprint.setPi(existingPI.get());
            return sprintRepository.save(sprint);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PI does not exist!");
        }
    }

    public Optional<Sprint> getSprintByName(String sprintName) {
        return sprintRepository.findByName(sprintName);
    }

    public Iterable<Sprint> saveAll(List<Sprint> sprints, Integer piId) {
        List<Sprint> toBeSaved = sprints.stream().filter(sprint -> {
            return StreamSupport.stream(sprintRepository.findAllByPiId(piId).spliterator(),false).filter(savedSprint-> savedSprint.getName().equalsIgnoreCase(sprint.getName())).count()==0;
        }).collect(Collectors.toList());
        return sprintRepository.saveAll(toBeSaved);
    }
}
