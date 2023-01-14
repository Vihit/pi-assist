package com.digitedgy.piassist.model;

import java.sql.Date;

public class SprintModel {

    private int id;
    private String state;
    private String name;
    private Date stateDate;
    private Date endDate;
    private int originBoardId;

    public SprintModel() {
    }

    public SprintModel(int id, String state, String name, Date stateDate, Date endDate, int originBoardId) {
        this.id = id;
        this.state = state;
        this.name = name;
        this.stateDate = stateDate;
        this.endDate = endDate;
        this.originBoardId = originBoardId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStateDate() {
        return stateDate;
    }

    public void setStateDate(Date stateDate) {
        this.stateDate = stateDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getOriginBoardId() {
        return originBoardId;
    }

    public void setOriginBoardId(int originBoardId) {
        this.originBoardId = originBoardId;
    }
}
