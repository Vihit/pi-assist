package com.digitedgy.piassist.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class SupportActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String stream;
    private String type;
    private Integer storyPoints;
    private Double allocated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id", referencedColumnName = "id")
    private Sprint sprint;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(Integer storyPoints) {
        this.storyPoints = storyPoints;
    }

    public Double getAllocated() {
        return allocated;
    }

    public void setAllocated(Double allocated) {
        this.allocated = allocated;
    }

    @JsonBackReference
    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }
}
