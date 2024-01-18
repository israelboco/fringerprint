package com.presence.testpresence.ws;

import lombok.Data;

import java.util.Date;

@Data
public class PresenceEmployeeWs {

    private Integer id;
    private Integer employeeId;
    private Date created;
    private String day;
    private String month;
    private String year;
    private String hour;

}
