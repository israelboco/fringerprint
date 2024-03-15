package com.presence.testpresence.ws;

import lombok.Data;

@Data
public class ConnexionWs {

    private Integer id;
    private UserWs user;
    private String token;
    private Boolean active;
    private String deviceSerial;

}
