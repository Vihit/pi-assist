package com.digitedgy.piassist.repository;

import com.digitedgy.piassist.entity.Sprint;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SprintRepository extends CrudRepository<Sprint, Integer> {
    public Iterable<Sprint> findAllByPiId(Integer id);
    public Optional<Sprint> findByName(String name);
}
