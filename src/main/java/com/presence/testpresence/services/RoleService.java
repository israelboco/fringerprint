package com.presence.testpresence.services;

import com.google.gson.Gson;
import com.presence.testpresence.model.entities.Role;
import com.presence.testpresence.model.repositories.RoleRepository;
import com.presence.testpresence.ws.ReponseWs;
import com.presence.testpresence.ws.RoleWs;
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
        role.setLabel(label);
        role.setDescription(description);
        roleRepository.save(role);
        return new ReponseWs("success", "create", 200, role.getLabel());
    }

    public ReponseWs update(Integer id, String label, String description){
        Role role = roleRepository.findOneById(id);
        if(role == null) return new ReponseWs("failed", "role not found", 404, null);
        role.setLabel(label);
        role.setDescription(description);
        roleRepository.save(role);
        return new ReponseWs("success", "update", 200, role.getLabel());
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
        List<RoleWs> roleWs = role.stream().map(ro -> gson.fromJson(gson.toJson(ro), RoleWs.class)).collect(Collectors.toList());
        return new ReponseWs("success", "list", 200, roleWs);
    }


}
