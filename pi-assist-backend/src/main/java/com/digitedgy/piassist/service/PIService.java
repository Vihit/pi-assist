package com.digitedgy.piassist.service;

import com.digitedgy.piassist.entity.PI;
import com.digitedgy.piassist.repository.PIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class PIService {

    @Autowired
    PIRepository piRepository;

    public PI save(PI pi) {
        if(pi.getId() == null) {
            if(StreamSupport.stream(piRepository.findAllByIsClosed(false).spliterator(),false).count() == 0)
                return piRepository.save(pi);
            else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An open PI already exists!");
        } else {
            return piRepository.save(pi);
        }
    }

    public Optional<PI> getPIByName(String name) {
        return piRepository.getPIByName(name);
    }

    public Iterable<PI> getAllOpenPIs() {
        return piRepository.findAllByIsClosed(false);
    }

    public Iterable<PI> getAll() {
        return piRepository.findAll();
    }

    public Optional<PI> findById(Integer id) {
        return piRepository.findById(id);
    }

    public Optional<PI> findOpenPIForTeam(String team) {
        return piRepository.findTopByTeamAndIsClosed(team,false);
    }
}
