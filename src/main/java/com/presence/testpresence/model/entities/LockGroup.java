package com.presence.testpresence.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "lock_group")
public class LockGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "group1")
	private int group1;
    @Column(name = "group2")
	private int group2;
    @Column(name = "group3")
	private int group3;
    @Column(name = "group4")
	private int group4;
    @Column(name = "group5")
	private int group5;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getGroup1() {
		return group1;
	}
	public void setGroup1(int group1) {
		this.group1 = group1;
	}
	public int getGroup2() {
		return group2;
	}
	public void setGroup2(int group2) {
		this.group2 = group2;
	}
	public int getGroup3() {
		return group3;
	}
	public void setGroup3(int group3) {
		this.group3 = group3;
	}
	public int getGroup4() {
		return group4;
	}
	public void setGroup4(int group4) {
		this.group4 = group4;
	}
	public int getGroup5() {
		return group5;
	}
	public void setGroup5(int group5) {
		this.group5 = group5;
	}


}
