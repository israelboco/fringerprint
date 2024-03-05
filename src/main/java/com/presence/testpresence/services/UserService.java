package com.presence.testpresence.services;

import com.presence.testpresence.model.entities.Connexion;
import com.presence.testpresence.model.entities.User;
import com.presence.testpresence.model.repositories.ConnexionRepository;
import com.presence.testpresence.model.repositories.UserRepository;
import com.presence.testpresence.ws.ReponseWs;
import com.presence.testpresence.ws.UserWs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Random;

@Component
public class UserService {

    private static Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    ConnexionRepository connexionRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    public ReponseWs login(String email, String password){
        User user = this.userRepository.findOneByEmail(email);
        if (user == null) return new ReponseWs("failed", "user not found", 404, null);
        boolean isPssw = this.passwordEncoder.matches(password, user.getPassword());
        Connexion connexion = new Connexion();
        if(isPssw){
            byte[] array = new byte[7];
            new Random().nextBytes(array);
            String generatedString = new String(array, Charset.forName("UTF-8"));
            connexion = this.connexionRepository.findByUserAndActive(user, true);
            if (connexion == null) {
                connexion.setUser(user);
                connexion.setActive(true);
                connexion.setCreated(new Date());
                connexion.setToken(generatedString+user.getEmail());
                this.connexionRepository.save(connexion);
            }
        }
        logger.debug("generate {} ", connexion.getToken());
        return new ReponseWs("succes", "user login", 200, connexion.getToken());
    }


    public ReponseWs register(UserWs ws){
        logger.debug("user {} ", ws);
        String password = this.passwordEncoder.encode(ws.getPassword());
        Gson gson= new Gson();
        User user = gson.fromJson(gson.toJson(ws), User.class);
        user.setNom(ws.getNom());
        user.setPrenom(ws.getPrenom());
        user.setEmail(ws.getEmail());
        user.setPassword(password);
        this.userRepository.save(user);
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        Connexion connexion = this.connexionRepository.findByUserAndActive(user, true);
        if (connexion == null) {
            connexion = new Connexion();
            connexion.setUser(user);
            connexion.setActive(true);
            connexion.setCreated(new Date());
            connexion.setToken(generatedString+user.getEmail());
            this.connexionRepository.save(connexion);
        }
        return new ReponseWs("success", "user register", 200, connexion.getToken());
    }


}
