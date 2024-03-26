package com.presence.testpresence.ws;

import com.presence.testpresence.model.enums.PresenceEnum;
import lombok.Data;

@Data
public class ConnexionWs {

    private Integer id;
    private UserWs user;
    private EmployeeWs employeeWs;
    private Boolean isAdmin;
    private String token;
    private String company;
    private Boolean active;
    private String deviceSerial;
    private Boolean confirmDemande;
    private long dateTimestamp;
    private PresenceEnum presence;

}
