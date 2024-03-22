package com.presence.testpresence.ws;

import lombok.Data;


@Data
public class ConversationWs {

    private Integer id;
    private String contenu;
    private Integer senderId;
    private Integer receiverId;
    private long dateTimestamp;
}
