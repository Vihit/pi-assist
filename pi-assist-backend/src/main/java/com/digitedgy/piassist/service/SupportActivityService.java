package com.digitedgy.piassist.service;

import com.digitedgy.piassist.entity.Sprint;
import com.digitedgy.piassist.entity.SupportActivity;
import com.digitedgy.piassist.repository.SupportActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class SupportActivityService {

    @Autowired
    SupportActivityRepository supportActivityRepository;

    @Autowired
    SprintService sprintService;

    public Iterable<SupportActivity> getAllForSprint(String sprintName) {
        Optional<Sprint> sprint = sprintService.getSprintByName(sprintName);
        if(sprint.isPresent()) {
            return supportActivityRepository.findAllBySprintId(sprint.get().getId());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Sprint does not exist!");
        }
    }

    public SupportActivity save(SupportActivity supportActivity, String sprintName) {
        Optional<Sprint> sprint = sprintService.getSprintByName(sprintName);
        if(sprint.isPresent()) {
            supportActivity.setSprint(sprint.get());
            return supportActivityRepository.save(supportActivity);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Sprint does not exist!");
        }
    }

    public void delete(Integer supportActivityId) {
        supportActivityRepository.deleteById(supportActivityId);
    }
}
