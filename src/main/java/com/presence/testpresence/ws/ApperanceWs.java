package com.presence.testpresence.ws;

import lombok.Data;

import java.util.Date;

@Data
public class ApperanceWs {

    private Integer id;
    private EmployeeWs employee;
    private CompanieWs companie;
    private Date created;
    private Date start;
    private Date finish;


}
