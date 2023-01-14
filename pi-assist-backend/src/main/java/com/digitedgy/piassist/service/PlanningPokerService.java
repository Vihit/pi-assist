package com.digitedgy.piassist.service;

import com.digitedgy.piassist.entity.PlanningPoker;
import com.digitedgy.piassist.repository.PlanningPokerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class PlanningPokerService {

    @Autowired
    PlanningPokerRepository planningPokerRepository;

    public PlanningPoker save(PlanningPoker planningPoker) {
        Optional<PlanningPoker> openItem = getOpenItems(planningPoker.getTeam());
        if(!openItem.isPresent()) {
            return planningPokerRepository.save(planningPoker);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An item is already being estimated in Planning Poker!");
        }
    }

    public Optional<PlanningPoker> getOpenItems(String team) {
        return planningPokerRepository.findTopByClosedAndTeam(false, team);
    }

    public PlanningPoker update(PlanningPoker planningPoker) {
        Optional<PlanningPoker> existingItem = planningPokerRepository.findTopByKeyOrderByIdDesc(planningPoker.getKey());
        if(existingItem.isPresent()) {
            if(planningPoker.getClosed() || planningPoker.getClosed() == existingItem.get().getClosed()) {
                planningPoker.setId(existingItem.get().getId());
                planningPoker.setSummary(existingItem.get().getSummary());
                planningPoker.setPlanningPokerEstimates(existingItem.get().getPlanningPokerEstimates());
                return planningPokerRepository.save(planningPoker);
            } else {
                return existingItem.get();
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such item in Planning Poker!");
        }
    }

    public Optional<PlanningPoker> getByKey(String key) {
        return planningPokerRepository.findTopByKeyOrderByIdDesc(key);
    }
}
