package com.presence.testpresence.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "user_temp")
public class UserTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "enrollId")
	int enrollId;
    @Column(name = "admin")
	int admin;
    @Column(name = "backupnum")
	int backupnum;

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
	public int getAdmin() {
		return admin;
	}
	public void setAdmin(int admin) {
		this.admin = admin;
	}
	public int getBackupnum() {
		return backupnum;
	}
	public void setBackupnum(int backupnum) {
		this.backupnum = backupnum;
	}
	@Override
	public String toString() {
		return "UserTemp [enrollId=" + enrollId + ", admin=" + admin
				+ ", backupnum=" + backupnum + "]";
	}


}
