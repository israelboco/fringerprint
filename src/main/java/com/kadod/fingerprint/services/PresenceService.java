package com.kadod.fingerprint.services;

import com.google.gson.Gson;
import com.kadod.commons.enums.Constant;
import com.kadod.commons.enums.PresenceEnum;
import com.kadod.commons.ws.*;
import com.kadod.database.model.entities.*;
import com.kadod.database.model.repositories.*;
import com.kadod.fingerprint.util.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PresenceService {

    private static Logger logger = LogManager.getLogger(PresenceService.class);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    PresenceRepository presenceRepository;
    @Autowired
    ConnexionRepository connexionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    EnrollInfoRepository enrollInfoRepository;

    public ReponseWs create(String token){
        Connexion connexion = this.connexionRepository.findByTokenAndActive(token, true);
        if(connexion == null) return new ReponseWs("failed", "user not found", 404, null);
        Presence presence= new Presence();
        presence.setCreated(new Date());
        presence.setUser(connexion.getUser());
        this.presenceRepository.save(presence);
        return new ReponseWs("success", "create", 200, presence.getId());
    }

    public ReponseWs create(Records records){
        try {
            List<EnrollInfo> enrollInfo = this.enrollInfoRepository.findByEnrollId(records.getEnrollId());
            Employee employee = this.employeeRepository.findByEnrollInfo(enrollInfo.get(0));
            Presence presence = new Presence();
            presence.setCreated(dateFormat.parse(records.getRecordsTime()));
            presence.setUser(employee.getUser());
            presence.setRecord(records);
            this.presenceRepository.save(presence);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ReponseWs("success", "create", 200, null);
    }

    public ReponseWs list(String token){
        Gson gson = new Gson();
        String email = JwtUtil.extractEmail(token);
        User user = userRepository.findOneByEmail(email);
        if(user == null) return new ReponseWs(Constant.FAILED, "user not found", 404, null);
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

    public ReponseWs find(String token, String date, Integer userID)  {
        System.out.println(date);
        Date dataNow = new Date();
        try{
            if(date != null){
                dataNow = dateFormat.parse(date);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(dataNow);
        Calendar car = Calendar.getInstance();
        car.setTime(dataNow);
        Instant instantFromCalendar = car.toInstant();
        ZonedDateTime zonedDateTimeFromCalendar = instantFromCalendar.atZone(ZoneId.systemDefault());
        LocalDate localDateFromCalendar = zonedDateTimeFromCalendar.toLocalDate();
        System.out.println(localDateFromCalendar);
        LocalDate localNow = LocalDate.of(localDateFromCalendar.getYear(), localDateFromCalendar.getMonthValue(), localDateFromCalendar.getDayOfMonth());
        LocalDate debutJournee = localNow.atStartOfDay().toLocalDate();
        LocalDateTime finJournee = localNow.atTime(23, 59, 59, 999999999);
        User user = new User();
        System.out.println(userID);
        if(userID != null){
            user = userRepository.findOneById(userID);
            System.out.println(user.getEmail());
        }else {
            String email = JwtUtil.extractEmail(token);
            user = userRepository.findOneByEmail(email);
            System.out.println(user.getEmail());
        }
        if(user == null) return new ReponseWs("failed", "user not found", 404, null);
        Connexion connexion = connexionRepository.findByUser(user);
        PresenceEnum present = PresenceEnum.NON_DEFINIE;
        Date dateDEBUT= Date.from(debutJournee.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateFIN= Date.from(finJournee.atZone(ZoneId.systemDefault()).toInstant());
        Presence presence = this.presenceRepository.findByUserAndCreatedBetween(user, dateDEBUT, dateFIN);
        String hours = null;
        LocalTime hourLimit = LocalTime.of(8, 5);
        System.out.println(hourLimit.toString());
        if(presence != null) {
            Calendar carHour = Calendar.getInstance();
            carHour.setTime(presence.getCreated());
            int hour = carHour.get(Calendar.HOUR_OF_DAY);
            int minute = carHour.get(Calendar.MINUTE);
            LocalTime hourNow = LocalTime.of(hour, minute);
            if (hourNow.isAfter(hourLimit)) {
                present = PresenceEnum.EN_RETARD;
                hours = hourNow.toString();
            } else {
                present = PresenceEnum.A_HEURE;
                hours = hourNow.toString();
            }
        }
        if(presence == null){
            if(dataNow.after(connexion.getCreated())){
                if (finJournee.isBefore(LocalDateTime.now())) {
                    present = PresenceEnum.ABSENT;
                }
            }
        }
        JourWs jourWs = new JourWs();
        jourWs.setJour(String.valueOf(debutJournee.getDayOfMonth()));
        jourWs.setMois(String.valueOf(debutJournee.getMonthValue()));
        jourWs.setAnnee(String.valueOf(debutJournee.getYear()));
        jourWs.setHours(hours);
        jourWs.setPresence(present);
        return new ReponseWs("success", "presence find", 200, jourWs);
    }

    public ReponseWs listPresenceMonth(String token, String date, Integer page , Integer size){
        Gson gson = new Gson();
        Pageable pageable = PageRequest.of(page, size);
        String emailAdmin = JwtUtil.extractEmail(token);
        User userAdmin = userRepository.findOneByEmail(emailAdmin);
        if(userAdmin == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
        Employee employeeAdmin = employeeRepository.findByUser(userAdmin);
        if(employeeAdmin == null) return new ReponseWs(Constant.FAILED, "employer invalide", 404, null);
        Page<Connexion> connexionPage = connexionRepository.findByConfirmDemandeAndCompany(true, employeeAdmin.getCompanie().getNom(), pageable);
        List<RapportPresenceWs> rapportPresenceWs = new ArrayList<>();
        for (Connexion connexion: connexionPage){
            RapportPresenceWs rapport = new RapportPresenceWs();
            ReponseWs reponseWs = this.find(token, date, connexion.getUser().getId());
            JourWs jourWs = gson.fromJson(gson.toJson(reponseWs.getData()), JourWs.class);
            Employee employee = employeeRepository.findByUser(connexion.getUser());
            if(employee != null)
                rapport.setEmployeeWs(gson.fromJson(gson.toJson(employee), EmployeeWs.class));
            rapport.setJourWs(jourWs);
            rapportPresenceWs.add(rapport);
        }
        PageImpl<RapportPresenceWs> rapportPresenceWsPage = new PageImpl<>(rapportPresenceWs, pageable, connexionPage.getTotalPages());
        return new ReponseWs(Constant.SUCCESS, "liste des presence d'un mois", 200, rapportPresenceWsPage);
    }

    public ReponseWs presenceMonth(String token, String date, Integer userID){
        Gson gson = new Gson();
        Date dataNow = new Date();
        try{
            if(date != null){
                dataNow = dateFormat.parse(date);
            }
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
        List<JourWs> jourWsList = new ArrayList<>();
        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day); // Définir le jour
            Date jour = calendar.getTime(); // Obtenir la date correspondante
            String dateString = dateFormat.format(jour);
            ReponseWs reponseWs = new ReponseWs();
            if(userID != null){
                reponseWs = this.find(token, dateString, userID);
            }
            else{
                reponseWs = this.find(token, dateString, null);
            }
            JourWs jourWs = gson.fromJson(gson.toJson(reponseWs.getData()), JourWs.class);
            jourWsList.add(jourWs);
        }
        return new ReponseWs(Constant.SUCCESS, "liste des presence du mois", 200, jourWsList);
    }
}
