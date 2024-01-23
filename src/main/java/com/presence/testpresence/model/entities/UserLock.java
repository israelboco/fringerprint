package com.presence.testpresence.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "user_lock")
public class UserLock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "enrollId")
	private int enrollId;
    @Column(name = "weekZone")
	private int weekZone;
    @Column(name = "group")
	private int group;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getEnrollId() {
		return enrollId;
	}
	public void setEnrollId(int enrollId) {
		this.enrollId = enrollId;
	}
	public int getWeekZone() {
		return weekZone;
	}
	public void setWeekZone(int weekZone) {
		this.weekZone = weekZone;
	}
	public int getGroup() {
		return group;
	}
	public void setGroup(int group) {
		this.group = group;
	}


}
