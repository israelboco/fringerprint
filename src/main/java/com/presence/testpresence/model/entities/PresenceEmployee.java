package com.presence.testpresence.model.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "presence_employee")
public class PresenceEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "employees_id", referencedColumnName = "id")
    @ManyToOne
    private Employee employee;
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name = "records_time")
    private String recordsTime;
    @Column(name = "mode")
    private int mode;
    @Column(name = "intout")
    private int intout;
    @Column(name = "event")
    private int event;
    @Column(name = "deviceSerialNum")
    private String deviceSerialNum;
    @Column(name = "temperature")
    private double temperature;
    @Column(name = "image")
    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getRecordsTime() {
        return recordsTime;
    }

    public void setRecordsTime(String recordsTime) {
        this.recordsTime = recordsTime;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getIntout() {
        return intout;
    }

    public void setIntout(int intout) {
        this.intout = intout;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public String getDeviceSerialNum() {
        return deviceSerialNum;
    }

    public void setDeviceSerialNum(String deviceSerialNum) {
        this.deviceSerialNum = deviceSerialNum;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
