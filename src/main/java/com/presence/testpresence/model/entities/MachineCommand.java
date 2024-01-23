package com.presence.testpresence.model.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "machine_command")
public class MachineCommand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "serial")
    private String serial;
    @Column(name = "name")
    private String name;
    @Column(name = "content")
    private String content;
    @Column(name = "status")
    private Integer status;
    @Column(name = "sendStatus")
    private Integer sendStatus;
    @Column(name = "errCount")
    private Integer errCount;
    @Column(name = "runTime")
    private Date runTime;
    @Column(name = "gmtCrate")
    private Date gmtCrate;
    @Column(name = "gmtModified")
    private Date gmtModified;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    
    public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    
    public Integer getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(Integer sendStatus) {
		this.sendStatus = sendStatus;
	}

	
	public Integer getErrCount() {
		return errCount;
	}

	public void setErrCount(Integer errCount) {
		this.errCount = errCount;
	}

	public Date getRunTime() {
        return runTime;
    }

    public void setRunTime(Date runTime) {
        this.runTime = runTime;
    }

    public Date getGmtCrate() {
        return gmtCrate;
    }

    public void setGmtCrate(Date gmtCrate) {
        this.gmtCrate = gmtCrate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

	@Override
	public String toString() {
		return "MachineCommand [id=" + id + ", serial=" + serial + ", name=" + name + ", content=" + content
				+ ", status=" + status + ", sendStatus=" + sendStatus + ", errCount=" + errCount + ", runTime="
				+ runTime + ", gmtCrate=" + gmtCrate + ", gmtModified=" + gmtModified + "]";
	}
    
    
}