package com.digitedgy.piassist.repository;

import com.digitedgy.piassist.entity.PlanningPokerEstimate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PlanningPokerEstimateRepository extends CrudRepository<PlanningPokerEstimate, Integer> {
    @Query("select e from PlanningPokerEstimate e JOIN e.planningPoker p where e.username = ?1 and p.id = ?2")
    public Optional<PlanningPokerEstimate> findByUsernameAndPlanningPoker(String username, Integer id);
    public Iterable<PlanningPokerEstimate> findAllByPlanningPokerId(Integer id);
}
