package com.digitedgy.piassist.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

@Entity
public class PlanningPoker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "ppKey")
    private String key;
    private String summary;
    private String team;
    private boolean closed;

    @OneToMany(mappedBy = "planningPoker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<PlanningPokerEstimate> planningPokerEstimates;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public boolean getClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public Set<PlanningPokerEstimate> getPlanningPokerEstimates() {
        return planningPokerEstimates;
    }

    @JsonManagedReference
    public void setPlanningPokerEstimates(Set<PlanningPokerEstimate> planningPokerEstimates) {
        this.planningPokerEstimates = planningPokerEstimates;
    }
}
