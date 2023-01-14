package com.digitedgy.piassist.repository;

import com.digitedgy.piassist.entity.PI;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PIRepository extends CrudRepository<PI, Integer> {
    public Optional<PI> getPIByName(String name);
    public Iterable<PI> findAllByIsClosed(Boolean isClosed);
    public Optional<PI> findTopByTeamAndIsClosed(String team, Boolean isClosed);
}
