package com.presence.testpresence.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "enroll_info")
public class EnrollInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "enrollId")
    private Integer enrollId;
    @Column(name = "backupnum")
    private Integer backupnum;
    @Column(name = "imagePath")
    private String imagePath;
    @Column(name = "signatures")
    private String signatures;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEnrollId() {
        return enrollId;
    }

    public void setEnrollId(Integer enrollId) {
        this.enrollId = enrollId;
    }

    public Integer getBackupnum() {
        return backupnum;
    }

    public void setBackupnum(Integer backupnum) {
        this.backupnum = backupnum;
    }

    
  
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getSignatures() {
        return signatures;
    }

    public void setSignatures(String signatures) {
        this.signatures = signatures == null ? null : signatures.trim();
    }

	@Override
	public String toString() {
		return "EnrollInfo [id=" + id + ", enrollId=" + enrollId + ", backupnum=" + backupnum + ", imagePath="
				+ imagePath + ", signatures=" + signatures + "]";
	}


	
    
    
}