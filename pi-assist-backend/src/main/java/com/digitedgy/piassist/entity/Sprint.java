package com.digitedgy.piassist.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer jiraSprintId;
    private String name;
    private Date start;
    private Date end;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="pi_id", referencedColumnName = "id")
    private PI pi;

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SupportActivity> supportActivities;

    public Sprint() {
    }

    public Sprint(String name, Date start, Date end, PI pi, Integer jiraSprintId) {
        this.jiraSprintId = jiraSprintId;
        this.name = name;
        this.start = start;
        this.end = end;
        this.pi = pi;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJiraSprintId() {
        return jiraSprintId;
    }

    public void setJiraSprintId(Integer jiraSprintId) {
        this.jiraSprintId = jiraSprintId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @JsonBackReference
    public PI getPi() {
        return pi;
    }

    public void setPi(PI pi) {
        this.pi = pi;
    }

    public Set<SupportActivity> getSupportActivities() {
        return supportActivities;
    }

    @JsonManagedReference
    public void setSupportActivities(Set<SupportActivity> supportActivities) {
        this.supportActivities = supportActivities;
    }
}
