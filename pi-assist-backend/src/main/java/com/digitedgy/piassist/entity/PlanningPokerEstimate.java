package com.digitedgy.piassist.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "planning_poker_estimate")
public class PlanningPokerEstimate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private Integer estimate;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "planning_poker_id", referencedColumnName = "id")
    private PlanningPoker planningPoker;

    public PlanningPokerEstimate() {
    }

    public PlanningPokerEstimate(String username, Integer estimate) {
        this.username = username;
        this.estimate = estimate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getEstimate() {
        return estimate;
    }

    public void setEstimate(Integer estimate) {
        this.estimate = estimate;
    }

    @JsonBackReference
    public PlanningPoker getPlanningPoker() {
        return planningPoker;
    }

    public void setPlanningPoker(PlanningPoker planningPoker) {
        this.planningPoker = planningPoker;
    }
}
