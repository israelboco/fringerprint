package com.presence.testpresence.ws;

import lombok.Data;

import java.util.Date;

@Data
public class MachineCommandWs {

    private Integer id;
    private String serial;
    private String name;
    private String content;
    private Integer status;
    private Integer sendStatus;
    private Integer errCount;
    private Date runTime;
    private Date gmtCrate;
    private Date gmtModified;

}
