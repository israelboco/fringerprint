package com.presence.testpresence.services;

import com.google.gson.Gson;
import com.presence.testpresence.model.entities.Companie;
import com.presence.testpresence.model.entities.Connexion;
import com.presence.testpresence.model.entities.Employee;
import com.presence.testpresence.model.entities.User;
import com.presence.testpresence.model.enums.Constant;
import com.presence.testpresence.model.repositories.ConnexionRepository;
import com.presence.testpresence.model.repositories.EmployeeRepository;
import com.presence.testpresence.model.repositories.UserRepository;
import com.presence.testpresence.util.JwtUtil;
import com.presence.testpresence.ws.*;
import com.sun.net.httpserver.Authenticator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DemandeService {

    private static Logger logger = LogManager.getLogger(DemandeService.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    ConnexionRepository connexionRepository;


    public ReponseWs accept(String token, DemandeWs ws){
        String emailAdmin = JwtUtil.extractEmail(token);
        User userAdmin = userRepository.findOneByEmail(emailAdmin);
        if(userAdmin == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
        Employee employeeAdmin = this.employeeRepository.findByUser(userAdmin);
        User user = userRepository.findOneById(ws.getUserId());
        if(user == null) return new ReponseWs(Constant.FAILED, "L'utilisateur n'existe pas", 404, null);
        Connexion connexion = connexionRepository.findByUser(user);
        connexion.setActive(true);
        connexion.setConfirmDemande(true);
        connexionRepository.save(connexion);
        EmployeeWs employeeWs = new EmployeeWs();
        employeeWs.setIdCompany(employeeAdmin.getCompanie().getId());
        employeeWs.setNom(user.getNom());
        employeeWs.setPrenom(user.getPrenom());
        employeeWs.setEnrollId(ws.getEnrollId());
        employeeWs.setEmail(user.getEmail());
        employeeWs.setIsAdmin(true);
        employeeWs.setUser_id(user.getId());
        ReponseWs reponseWs = this.employeeService.saveEmployee(employeeWs);
        if (reponseWs.getStatus().equals(Constant.FAILED)) return reponseWs;
        return new ReponseWs(Constant.SUCCESS, "employee accepter avec SUCCESS", 200, null);
    }

    public ReponseWs acceptAdmin(DemandeWs ws){
//        String emailAdmin = JwtUtil.extractEmail(token);
//        User userAdmin = userRepository.findOneByEmail(emailAdmin);
//        if(userAdmin == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
//        Employee employeeAdmin = this.employeeRepository.findByUser(userAdmin);
        User user = userRepository.findOneById(ws.getUserId());
        if(user == null) return new ReponseWs(Constant.FAILED, "L'utilisateur n'existe pas", 404, null);
        Connexion connexion = connexionRepository.findByUser(user);
        connexion.setActive(true);
        connexion.setConfirmDemande(true);
        connexionRepository.save(connexion);
        EmployeeWs employeeWs = new EmployeeWs();
        employeeWs.setIdCompany(ws.getCompanyID());
        employeeWs.setNom(user.getNom());
        employeeWs.setPrenom(user.getPrenom());
        employeeWs.setEnrollId(ws.getEnrollId());
        employeeWs.setEmail(user.getEmail());
        employeeWs.setIsAdmin(true);
        employeeWs.setUser_id(user.getId());
        ReponseWs reponseWs = this.employeeService.saveEmployee(employeeWs);
        if (reponseWs.getStatus().equals(Constant.FAILED)) return reponseWs;
        return new ReponseWs(Constant.SUCCESS, "employee accepter avec SUCCESS", 200, null);
    }

    public ReponseWs refuse(String token, DemandeWs ws){
        String emailAdmin = JwtUtil.extractEmail(token);
        User userAdmin = userRepository.findOneByEmail(emailAdmin);
        if(userAdmin == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
        User user = userRepository.findOneById(ws.getUserId());
        if(user == null) return new ReponseWs(Constant.FAILED, "L'utilisateur n'existe pas", 404, null);
        Connexion connexion = connexionRepository.findByUser(user);
        connexion.setActive(true);
        connexion.setConfirmDemande(false);
        connexionRepository.save(connexion);
        return new ReponseWs(Constant.SUCCESS, "employee refuser avec SUCCESS", 200, null);

    }

    public ReponseWs listAccept(String token, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        String emailAdmin = JwtUtil.extractEmail(token);
        User userAdmin = userRepository.findOneByEmail(emailAdmin);
        Employee employeeAdmin = employeeRepository.findByUser(userAdmin);
        if(userAdmin == null || employeeAdmin == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
        Page<Connexion> connexionPage = connexionRepository.findByConfirmDemande(true, pageable);
        List<ConnexionWs> connexionWsList = connexionPage.stream().filter(d -> employeeRepository.findByUserAndCompanie(d.getUser(), employeeAdmin.getCompanie()) != null)
                .map(this::getConnexionWs).collect(Collectors.toList());
        PageImpl<ConnexionWs> connexionWsPage = new PageImpl<>(connexionWsList, pageable, connexionPage.getTotalPages());
        return new ReponseWs(Constant.SUCCESS, "Listes employees accepte", 200, connexionWsPage);
    }

    public ReponseWs listRefuser(String token, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        String emailAdmin = JwtUtil.extractEmail(token);
        User userAdmin = userRepository.findOneByEmail(emailAdmin);
        Employee employeeAdmin = employeeRepository.findByUser(userAdmin);
        if(userAdmin == null || employeeAdmin == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
        Page<Connexion> connexionPage = connexionRepository.findByConfirmDemande(false, pageable);
        List<ConnexionWs> connexionWsList = connexionPage.stream().filter(d -> employeeRepository.findByUserAndCompanie(d.getUser(), employeeAdmin.getCompanie()) != null)
                .map(this::getConnexionWs).collect(Collectors.toList());
        PageImpl<ConnexionWs> connexionWsPage = new PageImpl<>(connexionWsList, pageable, connexionPage.getTotalPages());
        return new ReponseWs(Constant.SUCCESS, "Listes employees refuser", 200, connexionWsPage);
    }

    public ReponseWs listDemande(String token, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        String emailAdmin = JwtUtil.extractEmail(token);
        User userAdmin = userRepository.findOneByEmail(emailAdmin);
        Employee employeeAdmin = employeeRepository.findByUser(userAdmin);
        if(userAdmin == null || employeeAdmin == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
        Page<Connexion> connexionPage = connexionRepository.findByCompanyAndConfirmDemandeIsNull(employeeAdmin.getCompanie().getNom(), pageable);
        List<ConnexionWs> connexionWsList = connexionPage.stream()
                .map(this::getConnexionWs).collect(Collectors.toList());
        PageImpl<ConnexionWs> connexionWsPage = new PageImpl<>(connexionWsList, pageable, connexionPage.getTotalPages());
        logger.debug(connexionWsList);
        return new ReponseWs(Constant.SUCCESS, "Listes demandes employees", 200, connexionWsPage);
    }

    private ConnexionWs getConnexionWs(Connexion connexion){
        Gson gson = new Gson();
        ConnexionWs connexionWs = gson.fromJson(gson.toJson(connexion), ConnexionWs.class);
        connexionWs.setDateTimestamp(connexion.getCreated().getTime());
        return connexionWs;
    }
}
