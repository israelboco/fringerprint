package com.kadod.fingerprint.services;

import com.kadod.database.model.entities.Connexion;
import com.kadod.database.model.entities.User;
import com.kadod.database.model.repositories.ConnexionRepository;
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
        if(connexion != null && dateNow.after(connexion.getDateExpireToken())) return connexion.getUser();
        return null;
    }
}
