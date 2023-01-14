package com.digitedgy.piassist.model;

public class Project {
    private Integer id;
    private String key;
    private String name;

    public Project(Integer id, String key, String name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }

    public Project(String key) {
        this.key = key;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
