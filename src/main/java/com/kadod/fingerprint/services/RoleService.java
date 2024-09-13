package com.kadod.fingerprint.services;

import com.google.gson.Gson;
import com.kadod.commons.ws.ReponseWs;
import com.kadod.commons.ws.RoleWs;
import com.kadod.database.model.entities.Role;
import com.kadod.database.model.repositories.RoleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleService {

    private static Logger logger = LogManager.getLogger(RoleService.class);

    @Autowired
    RoleRepository roleRepository;

    public ReponseWs create(String label, String description){
        Role role = new Role();
        role.setName(label);
        role.setDescription(description);
        roleRepository.save(role);
        return new ReponseWs("success", "create", 200, role.getName());
    }

    public ReponseWs update(Integer id, String label, String description){
        Role role = roleRepository.findOneById(id);
        if(role == null) return new ReponseWs("failed", "role not found", 404, null);
        role.setName(label);
        role.setDescription(description);
        roleRepository.save(role);
        return new ReponseWs("success", "update", 200, role.getName());
    }

    public ReponseWs delete(Integer id){
        Role role = roleRepository.findOneById(id);
        if(role == null) return new ReponseWs("failed", "role not found", 404, null);
        roleRepository.delete(role);
        return new ReponseWs("success", "delete", 200, null);
    }

    public ReponseWs list(Integer page, Integer size){
        Gson gson = new Gson();
        List<Role> role = roleRepository.findAll();
        List<RoleWs> roleWs = role.stream().map(this::getroleWs).collect(Collectors.toList());
        return new ReponseWs("success", "list", 200, roleWs);
    }

    public ReponseWs find(Integer id){
        Role role = roleRepository.findOneById(id);
        RoleWs roleWs = this.getroleWs(role);
        return new ReponseWs("success", "find", 200, roleWs);
    }

    private RoleWs getroleWs(Role role){
        Gson gson = new Gson();
        RoleWs roleWs = gson.fromJson(gson.toJson(role), RoleWs.class);
        roleWs.setName(role.getName());
        return roleWs;
    }


}
