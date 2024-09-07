package com.kadod.fingerprint.services;

import com.google.gson.Gson;
import com.kadod.commons.enums.Constant;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    @Autowired
    MachineRepository machineRepository;
    @Autowired
    FileService fileService;
    @Autowired
    PresenceService presenceService;
    @Autowired
    RoleRepository roleRepository;


    public ReponseWs accept(String token, DemandeWs ws){
        String emailAdmin = JwtUtil.extractEmail(token);
        User userAdmin = userRepository.findOneByEmail(emailAdmin);
        if(userAdmin == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
        Employee employeeAdmin = this.employeeRepository.findByUser(userAdmin);
        User user = userRepository.findOneById(ws.getUserId());
        if(user == null) return new ReponseWs(Constant.FAILED, "L'utilisateur n'existe pas", 404, null);
        if(ws.getDeviceSerial() == null)
            ws.setDeviceSerial(employeeAdmin.getEnrollInfo().getMachine().getSerialNo());
        Machine machine = machineRepository.findOneBySerialNo(ws.getDeviceSerial());
        if (machine == null) return new ReponseWs(Constant.FAILED, "device Serial not found", 404, null);
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
        employeeWs.setDeviceSerial(ws.getDeviceSerial());
        employeeWs.setIsAdmin(true);
        employeeWs.setUser_id(user.getId());
        ReponseWs reponseWs = this.employeeService.saveEmployee(employeeWs, employeeAdmin);
        if (reponseWs.getStatus().equals(Constant.FAILED)) return reponseWs;
        return new ReponseWs(Constant.SUCCESS, "employee accepter avec SUCCESS", 200, null);
    }

    public ReponseWs acceptAdmin(DemandeWs ws){
        User user = userRepository.findOneById(ws.getUserId());
        if(user == null) return new ReponseWs(Constant.FAILED, "L'utilisateur n'existe pas", 404, null);
        Set<Role> roles = new HashSet<>();
        Role role = this.roleRepository.findOneById(2);
        if (role != null){
            roles.add(role);
            user.setRoles(roles);
            this.userRepository.save(user);
        }
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
        employeeWs.setDeviceSerial(ws.getDeviceSerial());
        employeeWs.setIsAdmin(true);
        employeeWs.setUser_id(user.getId());
        ReponseWs reponseWs = this.employeeService.saveEmployee(employeeWs, null);
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
        if(userAdmin == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
        Employee employeeAdmin = employeeRepository.findByUser(userAdmin);
        if(employeeAdmin == null) return new ReponseWs(Constant.FAILED, "employer invalide", 404, null);
        Page<Connexion> connexionPage = connexionRepository.findByConfirmDemandeAndCompany(true, employeeAdmin.getCompanie().getNom(), pageable);
        List<ConnexionWs> connexionWsList = connexionPage.stream().filter(d -> employeeRepository.findByUserAndCompanie(d.getUser(), employeeAdmin.getCompanie()) != null)
                .map(this::getConnexionWs).collect(Collectors.toList());
        PageImpl<ConnexionWs> connexionWsPage = new PageImpl<>(connexionWsList, pageable, connexionPage.getTotalPages());
        return new ReponseWs(Constant.SUCCESS, "Listes employees accepte", 200, connexionWsPage);
    }

    public ReponseWs list(String token, String date, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        String emailAdmin = null;
        try{
            emailAdmin = JwtUtil.extractEmail(token);
        }catch (Exception e){
            e.printStackTrace();
        }
        User userAdmin = null;
        if (emailAdmin != null)
            userAdmin = userRepository.findOneByEmail(emailAdmin);
        if(userAdmin == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
        Employee employeeAdmin = employeeRepository.findByUser(userAdmin);
        if(employeeAdmin == null) return new ReponseWs(Constant.FAILED, "employer invalide", 404, null);
        Page<Connexion> connexionPage = connexionRepository.findByCompany(employeeAdmin.getCompanie().getNom(), pageable);
        List<ConnexionWs> connexionWsList = connexionPage.stream()
//                .filter(d -> employeeRepository.findByUserAndCompanie(d.getUser(), employeeAdmin.getCompanie()) != null)
                .map(v -> this.getConnexionWithPresenceWs(v, date)).collect(Collectors.toList());
        PageImpl<ConnexionWs> connexionWsPage = new PageImpl<>(connexionWsList, pageable, connexionPage.getTotalPages());
        return new ReponseWs(Constant.SUCCESS, "Listes employees total", 200, connexionWsPage);
    }

    public ReponseWs listRefuser(String token, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        String emailAdmin = JwtUtil.extractEmail(token);
        User userAdmin = userRepository.findOneByEmail(emailAdmin);
        Employee employeeAdmin = employeeRepository.findByUser(userAdmin);
        if(userAdmin == null || employeeAdmin == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
        Page<Connexion> connexionPage = connexionRepository.findByConfirmDemande(false, pageable);
        List<ConnexionWs> connexionWsList = connexionPage.stream().filter(d -> employeeRepository.findByUserAndCompanie(d.getUser(), employeeAdmin.getCompanie()) == null)
                .map(this::getConnexionWs).collect(Collectors.toList());
        PageImpl<ConnexionWs> connexionWsPage = new PageImpl<>(connexionWsList, pageable, connexionPage.getTotalPages());
        return new ReponseWs(Constant.SUCCESS, "Listes employees refuser", 200, connexionWsPage);
    }

    public ReponseWs listDemandeForCompanie(String token, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        String emailAdmin = null;
        try{
            emailAdmin = JwtUtil.extractEmail(token);
        }catch (Exception e){
            e.printStackTrace();
        }
        User userAdmin = new User();
        if (emailAdmin == null)
            return new ReponseWs(Constant.SUCCESS, "token invalide", 401, null);
        userAdmin = userRepository.findOneByEmail(emailAdmin);
        Employee employeeAdmin = new Employee();
        if (userAdmin != null)
            employeeAdmin = employeeRepository.findByUser(userAdmin);
        if(userAdmin == null || employeeAdmin == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
        Page<Connexion> connexionPage = connexionRepository.findByCompanyAndConfirmDemandeIsNull(employeeAdmin.getCompanie().getNom(), pageable);
        List<ConnexionWs> connexionWsList = connexionPage.stream()
                .map(this::getConnexionWs).collect(Collectors.toList());
        PageImpl<ConnexionWs> connexionWsPage = new PageImpl<>(connexionWsList, pageable, connexionPage.getTotalPages());
        logger.debug(connexionWsList);
        return new ReponseWs(Constant.SUCCESS, "Listes demandes employees", 200, connexionWsPage);
    }

    public ReponseWs listAll(String token, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        String emailAdmin = null;
        try{
            emailAdmin = JwtUtil.extractEmail(token);
        }catch (Exception e){
            e.printStackTrace();
        }
//        User userAdmin = new User();
//        if (emailAdmin == null)
//            return new ReponseWs(Constant.SUCCESS, "token invalide", 401, null);
//        userAdmin = userRepository.findOneByEmail(emailAdmin);
//        Employee employeeAdmin = new Employee();
//        if (userAdmin != null)
//            employeeAdmin = employeeRepository.findByUser(userAdmin);
        Page<Connexion> connexionPage = connexionRepository.findByConfirmDemandeIsNull(pageable);
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
        Employee employee = employeeRepository.findByUser(connexion.getUser());
        if (employee != null)
            connexionWs.setEmployeeWs(this.getEmployeeWs(employee));
        connexionWs.setUser(this.getUserWs(connexion));
        return connexionWs;
    }

    private ConnexionWs getConnexionWithPresenceWs(Connexion connexion, String date){
        Gson gson = new Gson();
        ConnexionWs connexionWs = gson.fromJson(gson.toJson(connexion), ConnexionWs.class);
        connexionWs.setDateTimestamp(connexion.getCreated().getTime());
        Employee employee = employeeRepository.findByUser(connexion.getUser());
        if (employee != null){
            connexionWs.setEmployeeWs(this.getEmployeeWs(employee));
            ReponseWs reponseWs = this.presenceService.find(null, date, employee.getUser().getId());
            JourWs jourWs = gson.fromJson(gson.toJson(reponseWs.getData()), JourWs.class);
            connexionWs.setJourWs(jourWs);
        }
        connexionWs.setUser(this.getUserWs(connexion));
        return connexionWs;
    }

    private EmployeeWs getEmployeeWs(Employee employee){
        Gson gson = new Gson();
        EmployeeWs employeeWs = gson.fromJson(gson.toJson(employee), EmployeeWs.class);
        employeeWs.setCompany(employee.getCompanie().getNom());
        employeeWs.setIdCompany(employee.getCompanie().getId());
        employeeWs.setEnrollId(employee.getEnrollInfo().getEnrollId());
        employeeWs.setUser_id(employee.getUser().getId());
        if(employee.getImageData() != null)
            employeeWs.setImageProfile(this.fileService.downloadImage(employee.getImageData()));
        return employeeWs;
    }

    public UserWs getUserWs(Connexion connexion){
        Gson gson = new Gson();
        UserWs userWs = gson.fromJson(gson.toJson(connexion.getUser()), UserWs.class);
        userWs.setCompany(connexion.getCompany());
        return userWs;
    }
}
