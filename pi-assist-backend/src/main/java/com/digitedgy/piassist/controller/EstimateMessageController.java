package com.digitedgy.piassist.controller;

import com.digitedgy.piassist.entity.PlanningPoker;
import com.digitedgy.piassist.entity.PlanningPokerEstimate;
import com.digitedgy.piassist.model.EstimateMessage;
import com.digitedgy.piassist.model.Online;
import com.digitedgy.piassist.service.PlanningPokerEstimateService;
import com.digitedgy.piassist.service.PlanningPokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class EstimateMessageController {

    @Autowired
    PlanningPokerEstimateService planningPokerEstimateService;

    @Autowired
    PlanningPokerService planningPokerService;

    @MessageMapping("/estimateMessage")
    @SendTo("/topic/estimates")
    public EstimateMessage getEstimate(@RequestBody EstimateMessage estimateMessage) {
        Optional<PlanningPoker> openItem = planningPokerService.getOpenItems(estimateMessage.getTeam());
        if(openItem.isPresent()) {
            String key = openItem.get().getKey();
            if(key.equalsIgnoreCase(estimateMessage.getStoryId()+"")) {
                PlanningPokerEstimate planningPokerEstimate = new PlanningPokerEstimate(estimateMessage.getName(),estimateMessage.getEstimate());
                planningPokerEstimate.setPlanningPoker(openItem.get());
                planningPokerEstimateService.save(planningPokerEstimate);
                return null;
            } else {
                return null;
            }
        }
        return null;
    }

    @MessageMapping("/available")
    @SendTo("/topic/online")
    public Online getOnlineMembers(@RequestBody Online online) {
        return online;
    }

    @MessageMapping("/end")
    @SendTo("/topic/estimates")
    public String endSession(@RequestBody String endMessage)  {
        return endMessage;
    }

    @MessageMapping("/get")
    @SendTo("/topic/estimates")
    public Iterable<PlanningPokerEstimate> getEstimates(String id) {
        return planningPokerEstimateService.getAllEstimatesForItem(Integer.parseInt(id));
    }
}
