package com.presence.testpresence.ws;

import lombok.Data;

import java.util.Date;

@Data
public class MachineWs {

    private Integer id;
    private String serialNo;
    private TypeMachineWs typeMachine;
    private String name;
    private String AdressMac;
    private String AdresseIp;
    private CompanieWs companie;
    private String active;
    private Date created;

}
