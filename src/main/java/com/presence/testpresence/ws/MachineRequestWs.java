package com.presence.testpresence.ws;

import lombok.Data;

@Data
public class MachineRequestWs {

    private Integer id;
    private String serialNo;
    private Integer typeMachineId;
    private String name;
    private String adressMac;
    private String adresseIp;
    private Integer companieId;
    private Boolean active;
    private long createdTimestamp;

}
