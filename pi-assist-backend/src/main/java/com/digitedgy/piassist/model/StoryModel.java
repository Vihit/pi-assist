package com.digitedgy.piassist.model;

public class StoryModel {
    private JiraStoryDetails fields;
    StoryLinks update;

    public StoryModel(JiraStoryDetails fields, StoryLinks update) {
        this.fields = fields;
        this.update = update;
    }

    public JiraStoryDetails getFields() {
        return fields;
    }

    public void setFields(JiraStoryDetails fields) {
        this.fields = fields;
    }

    public StoryLinks getUpdate() {
        return update;
    }

    public void setUpdate(StoryLinks update) {
        this.update = update;
    }
}
