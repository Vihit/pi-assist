package com.digitedgy.piassist.model;

import java.util.ArrayList;

public class JiraDetails {
    private ArrayList<String> labels;
    private ArrayList<IssueLink> issueLinks;
    private Assignee assignee;
    private String description;
    private String summary;
    private IssueType issueType;
    private double customfield_10002; //estimate
    private String customfield_11306; //acceptance_criteria
    private ArrayList<String> customfield_10005; //sprint object in string
    private Project project;

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }

    public ArrayList<IssueLink> getIssueLinks() {
        return issueLinks;
    }

    public void setIssueLinks(ArrayList<IssueLink> issueLinks) {
        this.issueLinks = issueLinks;
    }

    public Assignee getAssignee() {
        return assignee;
    }

    public void setAssignee(Assignee assignee) {
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

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public double getCustomfield_10002() {
        return customfield_10002;
    }

    public void setCustomfield_10002(double customfield_10002) {
        this.customfield_10002 = customfield_10002;
    }

    public String getCustomfield_11306() {
        return customfield_11306;
    }

    public void setCustomfield_11306(String customfield_11306) {
        this.customfield_11306 = customfield_11306;
    }

    public ArrayList<String> getCustomfield_10005() {
        return customfield_10005;
    }

    public void setCustomfield_10005(ArrayList<String> customfield_10005) {
        this.customfield_10005 = customfield_10005;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
