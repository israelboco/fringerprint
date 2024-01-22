package com.presence.testpresence.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "access_week")
public class AccessWeek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "serial")
    private String serial;
    @Column(name = "name")
    private String name;
    @Column(name = "sunday")
    private Integer sunday;
    @Column(name = "monday")
    private Integer monday;
    @Column(name = "tuesday")
    private Integer tuesday;
    @Column(name = "wednesday")
    private Integer wednesday;
    @Column(name = "thursday")
    private Integer thursday;
    @Column(name = "friday")
    private Integer friday;
    @Column(name = "saturday")
    private Integer saturday;

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
        this.serial = serial == null ? null : serial.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getMonday() {
        return monday;
    }

    public void setMonday(Integer monday) {
        this.monday = monday;
    }

    public Integer getTuesday() {
        return tuesday;
    }

    public void setTuesday(Integer tuesday) {
        this.tuesday = tuesday;
    }

    public Integer getWednesday() {
        return wednesday;
    }

    public void setWednesday(Integer wednesday) {
        this.wednesday = wednesday;
    }

    public Integer getThursday() {
        return thursday;
    }

    public void setThursday(Integer thursday) {
        this.thursday = thursday;
    }

    public Integer getFriday() {
        return friday;
    }

    public void setFriday(Integer friday) {
        this.friday = friday;
    }

    public Integer getSaturday() {
        return saturday;
    }

    public void setSaturday(Integer saturday) {
        this.saturday = saturday;
    }

    public Integer getSunday() {
        return sunday;
    }

    public void setSunday(Integer sunday) {
        this.sunday = sunday;
    }

	@Override
	public String toString() {
		return "AccessWeek [id=" + id + ", serial=" + serial + ", name=" + name
				+ ", monday=" + monday + ", tuesday=" + tuesday
				+ ", wednesday=" + wednesday + ", thursday=" + thursday
				+ ", friday=" + friday + ", saturday=" + saturday + ", sunday="
				+ sunday + "]";
	}
    
    
}