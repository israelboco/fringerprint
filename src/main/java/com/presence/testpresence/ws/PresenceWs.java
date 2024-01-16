package com.presence.testpresence.ws;

import lombok.Data;

import java.util.Date;

@Data
public class PresenceWs {

    private Integer id;
    private UserWs user;
    private Date created;

}
