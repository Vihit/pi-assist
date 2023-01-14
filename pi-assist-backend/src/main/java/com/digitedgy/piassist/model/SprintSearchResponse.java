package com.digitedgy.piassist.model;

import java.util.ArrayList;

public class SprintSearchResponse {

    private int maxResults;
    private int startAt;
    private boolean isLast;
    private ArrayList<SprintModel> values;

    public SprintSearchResponse() {
    }

    public SprintSearchResponse(int maxResults, int startAt, boolean isLast, ArrayList<SprintModel> values) {
        this.maxResults = maxResults;
        this.startAt = startAt;
        this.isLast = isLast;
        this.values = values;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getStartAt() {
        return startAt;
    }

    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }

    public boolean getIsLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public ArrayList<SprintModel> getValues() {
        return values;
    }

    public void setValues(ArrayList<SprintModel> values) {
        this.values = values;
    }
}
