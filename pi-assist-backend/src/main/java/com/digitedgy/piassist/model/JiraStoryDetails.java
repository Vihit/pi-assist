package com.digitedgy.piassist.model;

public class JiraStoryDetails {
    private String description;
    private String summary;
    private IssueType issueType;
    private double customfield_10002; //estimate
    private String customfield_11306; //acceptance_criteria
    private int customfield_10005; //sprint id
    private String customfield_15200; //team
    private Project project;

    public JiraStoryDetails(String description, String summary, IssueType issueType, double customfield_10002, String customfield_11306, int customfield_10005, String customfield_15200, Project project) {
        this.description = description;
        this.summary = summary;
        this.issueType = issueType;
        this.customfield_10002 = customfield_10002;
        this.customfield_11306 = customfield_11306;
        this.customfield_10005 = customfield_10005;
        this.customfield_15200 = customfield_15200;
        this.project = project;
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

    public int getCustomfield_10005() {
        return customfield_10005;
    }

    public void setCustomfield_10005(int customfield_10005) {
        this.customfield_10005 = customfield_10005;
    }

    public String getCustomfield_15200() {
        return customfield_15200;
    }

    public void setCustomfield_15200(String customfield_15200) {
        this.customfield_15200 = customfield_15200;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}

