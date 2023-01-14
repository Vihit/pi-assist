package com.digitedgy.piassist.model;

import java.util.Base64;

public class LoggedInUser {
    private String username;
    private String role;
    private String team;
    private String authToken;
    private String pwd;
    private String name;
    private int capacity;

    public LoggedInUser(String username, String name, String role, String team, String pwd, int capacity) {
        this.username = username;
        this.role = role;
        this.team = team;
        this.pwd = pwd;
        this.name = name;
        this.capacity = capacity;
        this.authToken = Base64.getEncoder().encodeToString((this.username+":"+this.pwd).getBytes());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
