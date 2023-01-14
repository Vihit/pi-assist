package com.digitedgy.piassist.controller;

import com.digitedgy.piassist.entity.PlanningPoker;
import com.digitedgy.piassist.entity.PlanningPokerEstimate;
import com.digitedgy.piassist.service.PlanningPokerEstimateService;
import com.digitedgy.piassist.service.PlanningPokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/planning-poker")
public class PlanningPokerController {

    @Autowired
    PlanningPokerService planningPokerService;

    @Autowired
    PlanningPokerEstimateService planningPokerEstimateService;


    @PostMapping(path = "/")
    public ResponseEntity<?> saveItem(@RequestBody PlanningPoker planningPoker) {
        return new ResponseEntity<>(planningPokerService.save(planningPoker), HttpStatus.CREATED);
    }

    @PutMapping(path = "/")
    public ResponseEntity<?> updateItem(@RequestBody PlanningPoker planningPoker) {
        return new ResponseEntity<>(planningPokerService.update(planningPoker), HttpStatus.OK);
    }

    @PostMapping(path = "/estimate/")
    public ResponseEntity<?> postEstimate(@RequestBody PlanningPokerEstimate planningPokerEstimate) {
        return new ResponseEntity<>(planningPokerEstimateService.save(planningPokerEstimate), HttpStatus.CREATED);
    }

    @GetMapping(path = "/open/{team}")
    public ResponseEntity<?> getOpenItem(@PathVariable String team) {
        Optional<PlanningPoker> openItem = planningPokerService.getOpenItems(team);
        if(openItem.isPresent()) {
            return new ResponseEntity<>(openItem.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/estimates/{key}")
    public ResponseEntity<?> getEstimaesFor(@PathVariable Integer key) {
        return new ResponseEntity<>(planningPokerEstimateService.getAllEstimatesForItem(key), HttpStatus.OK);
    }
}
