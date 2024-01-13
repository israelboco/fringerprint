package com.presence.testpresence.services;

import com.google.gson.Gson;
import com.presence.testpresence.model.entities.Connexion;
import com.presence.testpresence.model.entities.Presence;
import com.presence.testpresence.model.repositories.ConnexionRepository;
import com.presence.testpresence.model.repositories.PresenceRepository;
import com.presence.testpresence.ws.PresenceWs;
import com.presence.testpresence.ws.ReponseWs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Component
public class PresenceService {

    private static Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    PresenceRepository presenceRepository;
    @Autowired
    ConnexionRepository connexionRepository;

    public ReponseWs create(String token){
        Connexion connexion = this.connexionRepository.findByTokenAndActive(token, true);
        if(connexion == null) return new ReponseWs("failed", "user not found", 404, null);
        Presence presence= new Presence();
        presence.setCreated(new Date());
        presence.setUser(connexion.getUser());
        this.presenceRepository.save(presence);
        return new ReponseWs("success", "create", 200, presence.getId());
    }

    public ReponseWs list(String token){
        Gson gson = new Gson();
        Connexion connexion = this.connexionRepository.findByTokenAndActive(token, true);
        if(connexion == null) return new ReponseWs("failed", "user not found", 404, null);
        List<Presence> list = this.presenceRepository.findByUser(connexion.getUser());
        List<PresenceWs> listWs = list.stream().map(p -> gson.fromJson(gson.toJson(p), PresenceWs.class)).sorted(Comparator.comparingInt(PresenceWs::getId)).toList();
        return new ReponseWs("success", "list", 200, listWs);
    }

}
