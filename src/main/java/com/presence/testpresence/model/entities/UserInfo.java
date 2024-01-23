package com.presence.testpresence.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "enrollId")
	private int enrollId;
    @Column(name = "name")
	private String name;
    @Column(name = "backupnum")
	private int backupnum;
    @Column(name = "admin")
	private int admin;
    @Column(name = "imagePath")
	private String imagePath;
    @Column(name = "record")
	private String record;

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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getBackupnum() {
		return backupnum;
	}
	public void setBackupnum(int backupnum) {
		this.backupnum = backupnum;
	}
	public int getAdmin() {
		return admin;
	}
	public void setAdmin(int admin) {
		this.admin = admin;
	}


	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getRecord() {
		return record;
	}
	public void setRecord(String record) {
		this.record = record;
	}
	@Override
	public String toString() {
		return "UserInfo [enrollId=" + enrollId + ", name=" + name + ", backupnum=" + backupnum + ", admin=" + admin
				+ ", imagePath=" + imagePath + ", record=" + record + "]";
	}



}
