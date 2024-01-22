package com.presence.testpresence.ws;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class PresenceEmployeeWs {

    private Integer id;
    private Integer employeeId;
    private Date created;
    private String recordsTime;
    private int mode;
    private int intout;
    private int event;
    private String deviceSerialNum;
    private double temperature;
    private String image;

}
