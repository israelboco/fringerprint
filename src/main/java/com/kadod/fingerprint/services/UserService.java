package com.kadod.fingerprint.services;

import com.kadod.commons.enums.Constant;
import com.kadod.commons.ws.*;
import com.kadod.database.model.entities.*;
import com.kadod.database.model.repositories.*;
import com.kadod.fingerprint.util.JwtUtil;
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

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserService {

    private static Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
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
        if (user == null) return new ReponseWs(Constant.FAILED, "L'email n'existe pas. Veillez créer un nouveau compte", 404, null);
        boolean isPssw = this.passwordEncoder.matches(password, user.getPassword());
        Connexion connexion = new Connexion();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 24);
        if(!isPssw) return new ReponseWs(Constant.FAILED, "Mot de passe invalide", 401, null);
        String generatedString = JwtUtil.generateToken(email);
        connexion = this.connexionRepository.findByUser(user);
        ConnexionWs connexionWs = gson.fromJson(gson.toJson(connexion), ConnexionWs.class);
        if(!connexion.getActive()) return new ReponseWs(Constant.SUCCESS, "Vous êtes en cours d'approbation, veillez patienter.", 415, connexionWs);
        Employee employee = this.employeeRepository.findByUser(user);
        EmployeeWs employeeWs = gson.fromJson(gson.toJson(employee), EmployeeWs.class);
        connexion.setUser(user);
        connexion.setToken(generatedString);
        connexion.setDateExpireToken(cal.getTime());
        connexionRepository.save(connexion);
        connexionWs = gson.fromJson(gson.toJson(connexion), ConnexionWs.class);
        connexionWs.setEmployeeWs(employeeWs);
        connexionWs.setIsAdmin(employeeWs.getIsAdmin());
        connexionWs.setUser(this.getUserWs(user, connexion));
        return new ReponseWs(Constant.SUCCESS, "utilisateur connecté", 200, connexionWs);
    }

    public ReponseWs update(UserRequestWs ws){
        logger.debug("user {} ", ws);
        User user = this.userRepository.findOneByEmail(ws.getEmail());
        if (user == null) return new ReponseWs(Constant.FAILED, "user not found", 404, null);
        Companie companie = this.companieRepository.findOneByNomIgnoreCaseOrCodeIgnoreCase(ws.getCompany(), ws.getCompany());
        if (companie == null) return new ReponseWs(Constant.FAILED, "L'entreprise n'existe pas, veillez corriger", 404, null);
        Gson gson= new Gson();
        Set<Role> roles = new HashSet<>();
        for(Integer id: ws.getIdRoles()){
            Role role = this.roleRepository.findOneById(id);
            if(role != null)
                roles.add(role);
        }
        user = gson.fromJson(gson.toJson(ws), User.class);
        user.setNom(ws.getNom());
        user.setPrenom(ws.getPrenom());
        user.setEmail(ws.getEmail());
        user.setRoles(roles);
        if(!ws.getPassword().isEmpty()) {
            String password = this.passwordEncoder.encode(ws.getPassword());
            user.setPassword(password);
        }
        this.userRepository.save(user);
        Connexion connexion = connexionRepository.findByUser(user);
        ConnexionWs connexionWs = gson.fromJson(gson.toJson(connexion), ConnexionWs.class);
        connexionWs.setUser(this.getUserWs(user, connexion));
        return new ReponseWs(Constant.SUCCESS, "update user.", 200, connexionWs);
    }

    public ReponseWs managerRegister(UserRequestWs ws){
        logger.debug("user {} ", ws);
        User user = this.userRepository.findOneByEmail(ws.getEmail());
        if (user != null) return new ReponseWs(Constant.FAILED, "user existe dèjà, connectez-vous", 408, null);
        Companie companie = this.companieRepository.findOneByNomIgnoreCaseOrCodeIgnoreCase(ws.getCompany(), ws.getCompany());
        if (companie == null) return new ReponseWs(Constant.FAILED, "L'entreprise n'existe pas, veillez corriger", 404, null);
        String password = this.passwordEncoder.encode(ws.getPassword());
        Gson gson= new Gson();
        Set<Role> roles = new HashSet<>();
        Role role = this.roleRepository.findOneById(2);

        if (role != null)
            roles.add(role);
        user = new User();
        user.setNom(ws.getNom());
        user.setPrenom(ws.getPrenom());
        user.setEmail(ws.getEmail());
        user.setRoles(roles);
        user.setPassword(password);
        user = this.userRepository.save(user);
        String generatedString = JwtUtil.generateToken(ws.getEmail());
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
        connexion = this.connexionRepository.save(connexion);
        ConnexionWs connexionWs = gson.fromJson(gson.toJson(connexion), ConnexionWs.class);
        connexionWs.setUser(this.getUserWs(user, connexion));
        return new ReponseWs(Constant.SUCCESS, "Vous êtes en cours d'approbation, veillez patienter.", 200, connexionWs);
    }

    public ReponseWs register(UserRequestWs ws){
        logger.debug("user {} ", ws);
        User user = this.userRepository.findOneByEmail(ws.getEmail());
        if (user != null) return new ReponseWs(Constant.FAILED, "user existe dèjà, connectez-vous", 408, null);
        Companie companie = this.companieRepository.findOneByNomIgnoreCaseOrCodeIgnoreCase(ws.getCompany(), ws.getCompany());
        if (companie == null) return new ReponseWs(Constant.FAILED, "L'entreprise n'existe pas, veillez corriger", 404, null);
        String password = this.passwordEncoder.encode(ws.getPassword());
        Gson gson= new Gson();
        Set<Role> roles = new HashSet<>();
        if(ws.getIdRoles() != null) {
            for (Integer id : ws.getIdRoles()) {
                Role role = this.roleRepository.findOneById(id);
                if (role != null)
                    roles.add(role);
            }
        }
        user = new User();
        user.setNom(ws.getNom());
        user.setPrenom(ws.getPrenom());
        user.setEmail(ws.getEmail());
        user.setRoles(roles);
        user.setPassword(password);
        user = this.userRepository.save(user);
        String generatedString = JwtUtil.generateToken(ws.getEmail());
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
        connexion = this.connexionRepository.save(connexion);
        ConnexionWs connexionWs = gson.fromJson(gson.toJson(connexion), ConnexionWs.class);
        connexionWs.setUser(this.getUserWs(user, connexion));
        return new ReponseWs(Constant.SUCCESS, "Vous êtes en cours d'approbation, veillez patienter.", 200, connexionWs);
    }

    public ReponseWs refeshToken(String token){
        Gson gson= new Gson();
        Connexion connexion = this.connexionRepository.findByTokenAndActive(token, true);
        if(connexion == null) return new ReponseWs(Constant.FAILED, "token not found", 401, null);
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
        return new ReponseWs(Constant.SUCCESS, "refresh token", 200, connexionWs);
    }

    public ReponseWs getUser(String token){
        Gson gson= new Gson();
        String email = JwtUtil.extractEmail(token);
        User user = this.userRepository.findOneByEmail(email);
        if (user == null) return  new ReponseWs(Constant.FAILED, "token not found or expired", 401, null);
        Connexion connexion = this.connexionRepository.findByUser(user);
        UserWs userws = gson.fromJson(gson.toJson(user), UserWs.class);
        userws.setCompany(connexion.getCompany());
        return new ReponseWs(Constant.SUCCESS, "user", 200, userws);
    }

    public ReponseWs listUser(Integer page, Integer size){
        Gson gson= new Gson();
        Pageable pageable = PageRequest.of(page, size);
        Page<User> listUser = this.userRepository.findAll(pageable);
        List<UserWs> listUserWs = listUser.getContent().stream().map(this::getUserWs).collect(Collectors.toList());
        PageImpl<UserWs> userWsPage = new PageImpl<>(listUserWs, pageable, listUser.getTotalPages());
        return new ReponseWs(Constant.SUCCESS, "user", 200, userWsPage);
    }

    private UserWs getUserWs(User user){
        Gson gson= new Gson();
        Connexion connexion = this.connexionRepository.findByUser(user);
        UserWs userWs = gson.fromJson(gson.toJson(user), UserWs.class);
        if (connexion != null)
            userWs.setCompany(connexion.getCompany());
        return userWs;
    }

    public ReponseWs userAdmin(UserRequestWs ws){
        logger.debug("user {} ", ws);
        User user = this.userRepository.findOneByEmail(ws.getEmail());
        if (user != null) return new ReponseWs(Constant.FAILED, "user existe dèjà, connectez-vous", 408, null);
        Companie companie = this.companieRepository.findOneByNomIgnoreCaseOrCodeIgnoreCase(ws.getCompany(), ws.getCompany());
        if (companie == null) return new ReponseWs(Constant.FAILED, "L'entreprise n'existe pas, veillez corriger", 404, null);
        String password = this.passwordEncoder.encode(ws.getPassword());
        Gson gson= new Gson();
        user = gson.fromJson(gson.toJson(ws), User.class);
        user.setNom(ws.getNom());
        user.setPrenom(ws.getPrenom());
        user.setEmail(ws.getEmail());
        user.setPassword(password);
        this.userRepository.save(user);
        String generatedString = JwtUtil.generateToken(ws.getEmail());
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
        return new ReponseWs(Constant.SUCCESS, "Vous êtes en cours d'approbation, veillez patienter.", 200, connexionWs);
    }

    public ReponseWs find(Integer id){
        Gson gson = new Gson();
        User user = userRepository.findOneById(id);
        if(user == null) return new ReponseWs(Constant.FAILED, "user not found", 404, null);
        UserWs userWs = this.getUserWs(user);
        return new ReponseWs(Constant.SUCCESS, "find", 200, userWs);
    }

    public ReponseWs delete(Integer id){
        Gson gson = new Gson();
        User user = this.userRepository.findOneById(id);
        if(user == null) return new ReponseWs(Constant.FAILED, "delete", 404, null);
        userRepository.deleteById(id);
        return new ReponseWs(Constant.SUCCESS, "delete", 200, null);

    }

    public UserWs getUserWs(User user, Connexion connexion){
        Gson gson = new Gson();
        UserWs userWs = gson.fromJson(gson.toJson(user), UserWs.class);
        userWs.setCompany(connexion.getCompany());
        return userWs;
    }

}
