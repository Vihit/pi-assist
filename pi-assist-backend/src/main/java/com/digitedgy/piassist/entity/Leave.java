package com.digitedgy.piassist.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "leaves")
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "leaveDt")
    private Date leave;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getLeave() {
        return leave;
    }

    public void setLeave(Date leave) {
        this.leave = leave;
    }

    @JsonBackReference
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
