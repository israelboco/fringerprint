package com.presence.testpresence.services;

import com.google.gson.Gson;
import com.presence.testpresence.model.entities.Connexion;
import com.presence.testpresence.model.entities.Employee;
import com.presence.testpresence.model.entities.Presence;
import com.presence.testpresence.model.entities.User;
import com.presence.testpresence.model.enums.PresenceEnum;
import com.presence.testpresence.model.repositories.ConnexionRepository;
import com.presence.testpresence.model.repositories.EmployeeRepository;
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
    @Autowired
    EmployeeRepository employeeRepository;

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
        String email = JwtUtil.extractEmail(token);
        User user = userRepository.findOneByEmail(email);
        Connexion connexion = this.connexionRepository.findByUser(user);
        if(connexion == null) return new ReponseWs("failed", "user not found", 404, null);
        List<Presence> list = this.presenceRepository.findByUser(connexion.getUser());
        List<PresenceWs> listWs = list.stream().map(this::getPresenceWs).sorted(Comparator.comparingInt(PresenceWs::getId).reversed()).collect(Collectors.toList());
        return new ReponseWs("success", "list", 200, listWs);
    }

    private PresenceWs getPresenceWs(Presence presence){
        Gson gson = new Gson();
        Employee employee = employeeRepository.findByUser(presence.getUser());
        PresenceWs presenceWs = gson.fromJson(gson.toJson(presence), PresenceWs.class);
        presenceWs.setEmployeeWs(gson.fromJson(gson.toJson(employee), EmployeeWs.class));
        presenceWs.setDateTimestamp(presence.getCreated().getTime());
        return presenceWs;
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
        PresenceEnum present = PresenceEnum.ABSENT;
        Date dateDEBUT= Date.from(debutJournee.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateFIN= Date.from(finJournee.atZone(ZoneId.systemDefault()).toInstant());
        Presence presence = this.presenceRepository.findByUserAndCreatedBetween(user, dateDEBUT, dateFIN);
        if(presence != null) {
            Calendar carHour = Calendar.getInstance();
            carHour.setTime(presence.getCreated());
            int hour = carHour.get(Calendar.HOUR_OF_DAY);
            int minute = carHour.get(Calendar.MINUTE);
            LocalTime hourNow = LocalTime.of(hour, minute);
            LocalTime hourLimit = LocalTime.of(8, 5);
            if (hourNow.isAfter(hourLimit)) {
                present = PresenceEnum.EN_RETARD;
            } else {
                present = PresenceEnum.A_HEURE;
            }
        }
        JourWs jourWs = new JourWs();
        jourWs.setJour(String.valueOf(debutJournee.getDayOfMonth()));
        jourWs.setMois(String.valueOf(debutJournee.getMonthValue()));
        jourWs.setAnnee(String.valueOf(debutJournee.getYear()));
        jourWs.setPresence(present);
        return new ReponseWs("success", "presence find", 200, jourWs);
    }

    public ReponseWs presenceMonth(String token, String date){
        Date dataNow = new Date();
        try{
            dataNow = dateFormat.parse(date);
        }catch (Exception e) {
            e.printStackTrace();
        }
        Calendar car = Calendar.getInstance();
        car.setTime(dataNow);
        Instant instantFromCalendar = car.toInstant();
        ZonedDateTime zonedDateTimeFromCalendar = instantFromCalendar.atZone(ZoneId.systemDefault());
        LocalDate localDateFromCalendar = zonedDateTimeFromCalendar.toLocalDate();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, localDateFromCalendar.getYear()); // Définir l'année
        calendar.set(Calendar.MONTH, localDateFromCalendar.getMonthValue() - 1); // Définir le mois (0-indexé)

        // Obtenir le nombre de jours dans le mois
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        // Parcourir tous les jours du mois
        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day); // Définir le jour
            Date jour = calendar.getTime(); // Obtenir la date correspondante
            // Faites quelque chose avec la date...
        }
        return new ReponseWs();
    }
}
