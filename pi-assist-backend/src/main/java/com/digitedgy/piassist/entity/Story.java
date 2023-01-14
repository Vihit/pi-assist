package com.digitedgy.piassist.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "s_key")
    private String key;
    private String title;
    @Column(columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "text")
    private String acceptanceCriteria;
    private int estimate;
    private String sprint;
    private String assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", referencedColumnName = "id")
    private Feature feature;

    public Story() {
    }

    public Story(String key, String title, String description, String acceptanceCriteria, int estimate, String sprint, String assignee) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.acceptanceCriteria = acceptanceCriteria;
        this.estimate = estimate;
        this.sprint = sprint;
        this.assignee = assignee;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAcceptanceCriteria() {
        return acceptanceCriteria;
    }

    public void setAcceptanceCriteria(String acceptanceCriteria) {
        this.acceptanceCriteria = acceptanceCriteria;
    }

    public int getEstimate() {
        return estimate;
    }

    public void setEstimate(int estimate) {
        this.estimate = estimate;
    }

    public String getSprint() {
        return sprint;
    }

    public void setSprint(String sprint) {
        this.sprint = sprint;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @JsonBackReference
    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }
}
