package com.digitedgy.piassist.model;

public class AddLink {

    LinkType type;
    InwardIssue inwardIssue;

    public AddLink(LinkType type, InwardIssue inwardIssue) {
        this.type = type;
        this.inwardIssue = inwardIssue;
    }

    public LinkType getType() {
        return type;
    }

    public void setType(LinkType type) {
        this.type = type;
    }

    public InwardIssue getInwardIssue() {
        return inwardIssue;
    }

    public void setInwardIssue(InwardIssue inwardIssue) {
        this.inwardIssue = inwardIssue;
    }
}
