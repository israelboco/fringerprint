package com.presence.testpresence.ws;

import lombok.Data;


@Data
public class ConversationWs {

    private Integer id;
    private String contenu;
    private EmployeeWs sender;
    private String token;
    private EmployeeWs receiver;
    private long dateTimestamp;
}
