package com.digitedgy.piassist.model;

public class LinkType {
    private String name;
    private String inward;
    private String outward;

    public LinkType(String name, String inward, String outward) {
        this.name = name;
        this.inward = inward;
        this.outward = outward;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInward() {
        return inward;
    }

    public void setInward(String inward) {
        this.inward = inward;
    }

    public String getOutward() {
        return outward;
    }

    public void setOutward(String outward) {
        this.outward = outward;
    }
}
