package com.presence.testpresence.ws;

import lombok.Data;

@Data
public class MachineRequestWs {

    private Integer id;
    private String serialNo;
    private Integer typeMachineId;
    private String name;
    private String AdressMac;
    private String AdresseIp;
    private Integer companieId;
    private Boolean active;
    private long createdTimestamp;

}
