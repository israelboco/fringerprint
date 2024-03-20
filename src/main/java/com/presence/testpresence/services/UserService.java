package com.presence.testpresence.services;

import com.presence.testpresence.model.entities.Companie;
import com.presence.testpresence.model.entities.Connexion;
import com.presence.testpresence.model.entities.Employee;
import com.presence.testpresence.model.entities.User;
import com.presence.testpresence.model.repositories.CompanieRepository;
import com.presence.testpresence.model.repositories.ConnexionRepository;
import com.presence.testpresence.model.repositories.EmployeeRepository;
import com.presence.testpresence.model.repositories.UserRepository;
import com.presence.testpresence.util.JwtUtil;
import com.presence.testpresence.ws.ConnexionWs;
import com.presence.testpresence.ws.EmployeeWs;
import com.presence.testpresence.ws.ReponseWs;
import com.presence.testpresence.ws.UserWs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class UserService {

    private static Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    ConnexionRepository connexionRepository;
    @Autowired
    ConnexionService connexionService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CompanieRepository companieRepository;
    @Autowired
    EmployeeRepository employeeRepository;


    public ReponseWs login(String email, String password){
        Gson gson = new Gson();
        User user = this.userRepository.findOneByEmail(email);
        if (user == null) return new ReponseWs("failed", "L'email n'existe pas. Veillez créer un nouveau compte", 404, null);
        boolean isPssw = this.passwordEncoder.matches(password, user.getPassword());
        Connexion connexion = new Connexion();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 24);
        if(!isPssw) return new ReponseWs("failed", "Mot de passe invalide", 401, null);
        String generatedString = JwtUtil.generateToken(email);
        connexion = this.connexionRepository.findByUser(user);
        ConnexionWs connexionWs = gson.fromJson(gson.toJson(connexion), ConnexionWs.class);
        if(!connexion.getActive()) return new ReponseWs("success", "Vous êtes en cours d'approbation, veillez patienter.", 415, connexionWs);
        Employee employee = this.employeeRepository.findByUser(user);
        EmployeeWs employeeWs = gson.fromJson(gson.toJson(employee), EmployeeWs.class);
        connexion.setUser(user);
        connexion.setToken(generatedString);
        connexion.setDateExpireToken(cal.getTime());
        connexionRepository.save(connexion);
        connexionWs = gson.fromJson(gson.toJson(connexion), ConnexionWs.class);
        connexionWs.setEmployeeWs(employeeWs);
        connexionWs.setIsAdmin(employeeWs.getIsAdmin());
        return new ReponseWs("success", "user login", 200, connexionWs);
    }

    public ReponseWs update(UserWs ws){
        logger.debug("user {} ", ws);
        User user = this.userRepository.findOneByEmail(ws.getEmail());
        if (user == null) return new ReponseWs("failed", "user not found", 404, null);
        Companie companie = this.companieRepository.findOneByNomIgnoreCaseOrCodeIgnoreCase(ws.getCompany(), ws.getCompany());
        if (companie == null) return new ReponseWs("failed", "L'entreprise n'existe pas, veillez corriger", 404, null);
        Gson gson= new Gson();
        user = gson.fromJson(gson.toJson(ws), User.class);
        user.setNom(ws.getNom());
        user.setPrenom(ws.getPrenom());
        user.setEmail(ws.getEmail());
        if(!ws.getPassword().isEmpty()) {
            String password = this.passwordEncoder.encode(ws.getPassword());
            user.setPassword(password);
        }
        this.userRepository.save(user);
        Connexion connexion = connexionRepository.findByUser(user);
        ConnexionWs connexionWs = gson.fromJson(gson.toJson(connexion), ConnexionWs.class);
        return new ReponseWs("success", "update user.", 200, connexionWs);

    }

    public ReponseWs register(UserWs ws){
        logger.debug("user {} ", ws);
        User user = this.userRepository.findOneByEmail(ws.getEmail());
        if (user != null) return new ReponseWs("failed", "user existe dèjà, connectez-vous", 408, null);
        Companie companie = this.companieRepository.findOneByNomIgnoreCaseOrCodeIgnoreCase(ws.getCompany(), ws.getCompany());
        if (companie == null) return new ReponseWs("failed", "L'entreprise n'existe pas, veillez corriger", 404, null);
        String password = this.passwordEncoder.encode(ws.getPassword());
//        String password = ws.getPassword();
        Gson gson= new Gson();
        user = gson.fromJson(gson.toJson(ws), User.class);
        user.setNom(ws.getNom());
        user.setPrenom(ws.getPrenom());
        user.setEmail(ws.getEmail());
        user.setPassword(password);
        this.userRepository.save(user);
        String generatedString = JwtUtil.generateToken(ws.getEmail());
//        byte[] array = new byte[7];
//        new Random().nextBytes(array);
//        String generatedString = new String(array, Charset.forName("UTF-8"));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 24);
        Connexion connexion = new Connexion();
        connexion.setUser(user);
        connexion.setActive(false);
        connexion.setConfirmDemande(null);
        connexion.setCreated(new Date());
        connexion.setCompany(companie.getNom());
        connexion.setToken(generatedString);
        connexion.setDateExpireToken(cal.getTime());
        this.connexionRepository.save(connexion);
        ConnexionWs connexionWs = gson.fromJson(gson.toJson(connexion), ConnexionWs.class);
        return new ReponseWs("success", "Vous êtes en cours d'approbation, veillez patienter.", 200, connexionWs);
    }

    public ReponseWs refeshToken(String token){
        Gson gson= new Gson();
        Connexion connexion = this.connexionRepository.findByTokenAndActive(token, true);
        if(connexion == null) return new ReponseWs("failed", "token not found", 401, null);
        String generatedString = JwtUtil.generateToken(connexion.getUser().getEmail());
//        byte[] array = new byte[7];
//        new Random().nextBytes(array);
//        String generatedString = new String(array, Charset.forName("UTF-8"));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 24);
        connexion.setToken(generatedString);
        connexion.setDateExpireToken(cal.getTime());
        this.connexionRepository.save(connexion);
        ConnexionWs connexionWs = gson.fromJson(gson.toJson(connexion), ConnexionWs.class);
        return new ReponseWs("success", "refresh token", 200, connexionWs);
    }

    public ReponseWs getUser(String token){
        Gson gson= new Gson();
        String email = JwtUtil.extractEmail(token);
        User user = this.userRepository.findOneByEmail(email);
        if (user == null) return  new ReponseWs("failed", "token not found or expired", 401, null);
        Connexion connexion = this.connexionRepository.findByUser(user);
        UserWs userws = gson.fromJson(gson.toJson(user), UserWs.class);
        userws.setCompany(connexion.getCompany());
        return new ReponseWs("success", "user", 200, userws);
    }

    public ReponseWs listUser(Integer page, Integer size){
        Gson gson= new Gson();
        Pageable pageable = PageRequest.of(page, size);
        Page<User> listUser = this.userRepository.findAll(pageable);
        List<UserWs> listUserWs = listUser.getContent().stream().map(this::getUserWs).collect(Collectors.toList());
        PageImpl<UserWs> userWsPage = new PageImpl<>(listUserWs, pageable, listUser.getTotalPages());
        return new ReponseWs("success", "user", 200, userWsPage);
    }

    private UserWs getUserWs(User user){
        Gson gson= new Gson();
        return gson.fromJson(gson.toJson(user), UserWs.class);
    }

    public ReponseWs userAdmin(UserWs ws){
        logger.debug("user {} ", ws);
        User user = this.userRepository.findOneByEmail(ws.getEmail());
        if (user != null) return new ReponseWs("failed", "user existe dèjà, connectez-vous", 408, null);
        Companie companie = this.companieRepository.findOneByNomIgnoreCaseOrCodeIgnoreCase(ws.getCompany(), ws.getCompany());
        if (companie == null) return new ReponseWs("failed", "L'entreprise n'existe pas, veillez corriger", 404, null);
        String password = this.passwordEncoder.encode(ws.getPassword());
//        String password = ws.getPassword();
        Gson gson= new Gson();
        user = gson.fromJson(gson.toJson(ws), User.class);
        user.setNom(ws.getNom());
        user.setPrenom(ws.getPrenom());
        user.setEmail(ws.getEmail());
        user.setPassword(password);
        this.userRepository.save(user);
        String generatedString = JwtUtil.generateToken(ws.getEmail());
//        byte[] array = new byte[7];
//        new Random().nextBytes(array);
//        String generatedString = new String(array, Charset.forName("UTF-8"));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 24);
        Connexion connexion = new Connexion();
        connexion.setUser(user);
        connexion.setActive(false);
        connexion.setConfirmDemande(null);
        connexion.setCreated(new Date());
        connexion.setCompany(companie.getNom());
        connexion.setToken(generatedString);
        connexion.setDateExpireToken(cal.getTime());
        this.connexionRepository.save(connexion);
        ConnexionWs connexionWs = gson.fromJson(gson.toJson(connexion), ConnexionWs.class);
        return new ReponseWs("success", "Vous êtes en cours d'approbation, veillez patienter.", 200, connexionWs);
    }
}
