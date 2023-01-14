package com.digitedgy.piassist.model;

import java.util.ArrayList;

public class UpdateLinks {
    ArrayList<AddIssueLink> issueLinks;

    public UpdateLinks(ArrayList<AddIssueLink> issueLinks) {
        this.issueLinks = issueLinks;
    }

    public ArrayList<AddIssueLink> getIssueLinks() {
        return issueLinks;
    }

    public void setIssueLinks(ArrayList<AddIssueLink> issueLinks) {
        this.issueLinks = issueLinks;
    }
}
