package com.presence.testpresence.services;

import com.google.gson.Gson;
import com.presence.testpresence.model.entities.Role;
import com.presence.testpresence.model.entities.TypeMachine;
import com.presence.testpresence.model.repositories.RoleRepository;
import com.presence.testpresence.model.repositories.TypeMachineRepository;
import com.presence.testpresence.ws.ReponseWs;
import com.presence.testpresence.ws.RoleWs;
import com.presence.testpresence.ws.TypeMachineWs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TypeMachineService {

    private static Logger logger = LogManager.getLogger(TypeMachineService.class);

    @Autowired
    TypeMachineRepository typeMachineRepository;

    public ReponseWs create(String label, String description){
        TypeMachine typeMachine = new TypeMachine();
        typeMachine.setLabel(label);
        typeMachine.setDescription(description);
        typeMachineRepository.save(typeMachine);
        return new ReponseWs("success", "create", 200, typeMachine.getLabel());
    }

    public ReponseWs update(Integer id, String label, String description){
        TypeMachine typeMachine = typeMachineRepository.findOneById(id);
        if(typeMachine == null) return new ReponseWs("failed", "typeMachine not found", 404, null);
        typeMachine.setLabel(label);
        typeMachine.setDescription(description);
        typeMachineRepository.save(typeMachine);
        return new ReponseWs("success", "update", 200, typeMachine.getLabel());
    }

    public ReponseWs delete(Integer id){
        TypeMachine typeMachine = typeMachineRepository.findOneById(id);
        if(typeMachine == null) return new ReponseWs("failed", "typeMachine not found", 404, null);
        typeMachineRepository.delete(typeMachine);
        return new ReponseWs("success", "delete", 200, null);
    }

    public ReponseWs list(Integer page, Integer size){
        Gson gson = new Gson();
        List<TypeMachine> typeMachines = typeMachineRepository.findAll();
        List<TypeMachineWs> typeMachineWs = typeMachines.stream().map(this::getTypeMachineWs).collect(Collectors.toList());
        return new ReponseWs("success", "list", 200, typeMachineWs);
    }

    public ReponseWs find(Integer id){
        TypeMachine typeMachine = typeMachineRepository.findOneById(id);
        TypeMachineWs typeMachineWs = this.getTypeMachineWs(typeMachine);
        return new ReponseWs("success", "find", 200, typeMachineWs);
    }

    private TypeMachineWs getTypeMachineWs(TypeMachine typeMachine){
        Gson gson = new Gson();
        TypeMachineWs typeMachineWs = gson.fromJson(gson.toJson(typeMachine), TypeMachineWs.class);
        return typeMachineWs;
    }


}
