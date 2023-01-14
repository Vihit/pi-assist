package com.digitedgy.piassist.model;

import java.util.ArrayList;

public class FilterSearchResponse {
    private ArrayList<Jira> issues;

    public ArrayList<Jira> getIssues() {
        return issues;
    }

    public void setIssues(ArrayList<Jira> issues) {
        this.issues = issues;
    }
}
