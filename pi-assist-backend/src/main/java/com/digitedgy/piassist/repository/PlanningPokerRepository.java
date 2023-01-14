package com.digitedgy.piassist.repository;

import com.digitedgy.piassist.entity.PlanningPoker;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PlanningPokerRepository extends CrudRepository<PlanningPoker, Integer> {
    public Optional<PlanningPoker> findTopByClosedAndTeam(Boolean isClosed, String team);
    public Optional<PlanningPoker> findTopByKeyOrderByIdDesc(String key);
}
