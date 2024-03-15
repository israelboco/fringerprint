package com.presence.testpresence.services;

import com.presence.testpresence.model.entities.Connexion;
import com.presence.testpresence.model.entities.User;
import com.presence.testpresence.model.repositories.ConnexionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ConnexionService {

    @Autowired
    ConnexionRepository connexionRepository;

    public User findByToken(String token) {
        Connexion connexion = connexionRepository.findByTokenAndActive(token, true);
        Date dateNow = new Date();
        if(connexion != null && dateNow.after(connexion.getDate_expire_token())) return connexion.getUser();
        return null;
    }
}
