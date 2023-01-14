package com.digitedgy.piassist.service;

import com.digitedgy.piassist.entity.PlanningPoker;
import com.digitedgy.piassist.entity.PlanningPokerEstimate;
import com.digitedgy.piassist.repository.PlanningPokerEstimateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class PlanningPokerEstimateService {

    @Autowired
    PlanningPokerEstimateRepository planningPokerEstimateRepository;

    @Autowired
    PlanningPokerService planningPokerService;

    public PlanningPokerEstimate save(PlanningPokerEstimate planningPokerEstimate) {
        Optional<PlanningPoker> existingPlanningPoker = planningPokerService.getByKey(planningPokerEstimate.getPlanningPoker().getKey());
        Optional<PlanningPokerEstimate> existingEstimate = planningPokerEstimateRepository.findByUsernameAndPlanningPoker(planningPokerEstimate.getUsername(),planningPokerEstimate.getPlanningPoker().getId());
        if(existingPlanningPoker.isPresent() && !existingEstimate.isPresent()) {
            planningPokerEstimate.setPlanningPoker(existingPlanningPoker.get());
            return planningPokerEstimateRepository.save(planningPokerEstimate);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Item does not exist in Planning Poker!");
        }
    }

    public Iterable<PlanningPokerEstimate> getAllEstimatesForItem(Integer key) {
        return planningPokerEstimateRepository.findAllByPlanningPokerId(key);
    }
}
