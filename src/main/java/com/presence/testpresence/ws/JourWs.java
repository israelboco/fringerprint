package com.presence.testpresence.ws;

import com.presence.testpresence.model.enums.PresenceEnum;
import lombok.Data;

@Data
public class JourWs {

    private String jour;
    private String mois;
    private String annee;
    private PresenceEnum presence;

}
