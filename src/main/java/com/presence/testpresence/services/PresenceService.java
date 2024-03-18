package com.presence.testpresence.services;

import com.google.gson.Gson;
import com.presence.testpresence.model.entities.Connexion;
import com.presence.testpresence.model.entities.Employee;
import com.presence.testpresence.model.entities.Presence;
import com.presence.testpresence.model.entities.User;
import com.presence.testpresence.model.repositories.ConnexionRepository;
import com.presence.testpresence.model.repositories.PresenceRepository;
import com.presence.testpresence.model.repositories.UserRepository;
import com.presence.testpresence.util.JwtUtil;
import com.presence.testpresence.ws.EmployeeWs;
import com.presence.testpresence.ws.JourWs;
import com.presence.testpresence.ws.PresenceWs;
import com.presence.testpresence.ws.ReponseWs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PresenceService {

    private static Logger logger = LogManager.getLogger(UserService.class);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    PresenceRepository presenceRepository;
    @Autowired
    ConnexionRepository connexionRepository;
    @Autowired
    UserRepository userRepository;

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
        List<PresenceWs> listWs = list.stream().map(p -> gson.fromJson(gson.toJson(p), PresenceWs.class)).sorted(Comparator.comparingInt(PresenceWs::getId)).collect(Collectors.toList());
        return new ReponseWs("success", "list", 200, listWs);
    }

    public ReponseWs find(String token, String date)  {
        Date dataNow = new Date();
        try{
            dataNow = dateFormat.parse(date);
        }catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug(dataNow);
        Calendar car = Calendar.getInstance();
        car.setTime(dataNow);
        Instant instantFromCalendar = car.toInstant();
        ZonedDateTime zonedDateTimeFromCalendar = instantFromCalendar.atZone(ZoneId.systemDefault());
        LocalDate localDateFromCalendar = zonedDateTimeFromCalendar.toLocalDate();
        logger.debug(localDateFromCalendar);
        LocalDate localNow = LocalDate.of(localDateFromCalendar.getYear(), localDateFromCalendar.getMonthValue(), localDateFromCalendar.getDayOfMonth());
        LocalDate debutJournee = localNow.atStartOfDay().toLocalDate();
        LocalDateTime finJournee = localNow.atTime(23, 59, 59, 999999999);

        String email = JwtUtil.extractEmail(token);
        User user = userRepository.findOneByEmail(email);
        if(user == null) return new ReponseWs("failed", "user not found", 404, null);
        boolean present = false;
        Date dateDEBUT= Date.from(debutJournee.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateFIN= Date.from(finJournee.atZone(ZoneId.systemDefault()).toInstant());
        Presence presence = this.presenceRepository.findByUserAndCreatedBetween(user, dateDEBUT, dateFIN);
        if(presence != null) present = true;
        JourWs jourWs = new JourWs();
        jourWs.setJour(String.valueOf(debutJournee.getDayOfMonth()));
        jourWs.setMois(String.valueOf(debutJournee.getMonthValue()));
        jourWs.setAnnee(String.valueOf(debutJournee.getYear()));
        jourWs.setPresence(present);
        return new ReponseWs("success", "presence find", 200, jourWs);
    }
}
