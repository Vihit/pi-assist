package com.digitedgy.piassist.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

@Entity
public class PI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name;
    private String filter;
    private Date start;
    private Date end;
    private boolean isClosed;
    private Double spPerDay;
    private Integer boardId;
    private String team;
    @CreationTimestamp
    private Timestamp create_dt;

    @OneToMany(mappedBy = "pi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Sprint> sprints;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public Double getSpPerDay() {
        return spPerDay;
    }

    public void setSpPerDay(Double spPerDay) {
        this.spPerDay = spPerDay;
    }

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Timestamp getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(Timestamp create_dt) {
        this.create_dt = create_dt;
    }

    @JsonManagedReference
    public Set<Sprint> getSprints() {
        return sprints;
    }

    public void setSprints(Set<Sprint> sprints) {
        this.sprints = sprints;
    }
}
