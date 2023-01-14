package com.digitedgy.piassist.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
public class Feature {
    @Id
    private String id;
    private String labels;
    private String assignee;
    @Column(columnDefinition = "text")
    private String description;
    private String summary;
    @Column(columnDefinition = "text")
    private String acceptanceCriteria;
    @Column(columnDefinition = "text")
    private String discussion;
    private String comments;
    private String planningAssignee;
    @Column(name="discussion_dt")
    private Date discussionDt;
    private int estimate;
    private String isPlanned;
    private String sprint;
    private String pi;
    private String team;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Story> stories;

    public Feature() {
    }

    public Feature(String id, String labels, String assignee, String description, String summary, String acceptanceCriteria, String discussion, String comments, String planningAssignee, Date discussionDt, int estimate, String isPlanned, String team, String pi, Set<Story> stories) {
        this.id = id;
        this.labels = labels;
        this.assignee = assignee;
        this.description = description;
        this.summary = summary;
        this.acceptanceCriteria = acceptanceCriteria;
        this.discussion = discussion;
        this.comments = comments;
        this.planningAssignee = planningAssignee;
        this.discussionDt = discussionDt;
        this.estimate = estimate;
        this.isPlanned = isPlanned;
        this.sprint = sprint;
        this.pi = pi;
        this.team = team;
        this.stories = stories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAcceptanceCriteria() {
        return acceptanceCriteria;
    }

    public void setAcceptanceCriteria(String acceptanceCriteria) {
        this.acceptanceCriteria = acceptanceCriteria;
    }

    public String getDiscussion() {
        return discussion;
    }

    public void setDiscussion(String discussion) {
        this.discussion = discussion;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPlanningAssignee() {
        return planningAssignee;
    }

    public void setPlanningAssignee(String planningAssignee) {
        this.planningAssignee = planningAssignee;
    }

    public Date getDiscussionDt() {
        return discussionDt;
    }

    public void setDiscussionDt(Date discussionDt) {
        this.discussionDt = discussionDt;
    }

    public int getEstimate() {
        return estimate;
    }

    public void setEstimate(int estimate) {
        this.estimate = estimate;
    }

    public String getIsPlanned() {
        return isPlanned;
    }

    public void setIsPlanned(String isPlanned) {
        this.isPlanned = isPlanned;
    }

    public String getSprint() {
        return sprint;
    }

    public void setSprint(String sprint) {
        this.sprint = sprint;
    }

    public String getPi() {
        return pi;
    }

    public void setPi(String pi) {
        this.pi = pi;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Set<Story> getStories() {
        return stories;
    }

    @JsonManagedReference
    public void setStories(Set<Story> stories) {
        this.stories = stories;
    }
}
