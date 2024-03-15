package com.presence.testpresence.services;

import com.presence.testpresence.ws.DemandeWs;
import com.presence.testpresence.ws.ReponseWs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class DemandeService {

    private static Logger logger = LogManager.getLogger(DemandeService.class);

    public ReponseWs accept(DemandeWs ws){

        return new ReponseWs();
    }

}
