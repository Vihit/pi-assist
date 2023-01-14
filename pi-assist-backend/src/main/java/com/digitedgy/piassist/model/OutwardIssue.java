package com.digitedgy.piassist.model;

public class OutwardIssue {
    private String key;
    private JiraDetails fields;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public JiraDetails getFields() {
        return fields;
    }

    public void setFields(JiraDetails fields) {
        this.fields = fields;
    }
}
