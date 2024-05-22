package com.presence.testpresence.ws;

import lombok.Data;


@Data
public class ConversationRequestWs {

    private Integer id;
    private String contenu;
    private Integer senderId;
    private String token;
    private Integer receiverId;
    private long dateTimestamp;
}
