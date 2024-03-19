package com.presence.testpresence.services;

import com.google.gson.Gson;
import com.presence.testpresence.model.entities.CompanieType;
import com.presence.testpresence.model.repositories.CompanieTypeRepository;
import com.presence.testpresence.ws.CompanieTypeWs;
import com.presence.testpresence.ws.ReponseWs;
import com.presence.testpresence.ws.RoleWs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanieTypeService {

    private static Logger logger = LogManager.getLogger(CompanieTypeService.class);


    @Autowired
    CompanieTypeRepository companieTypeRepository;

    public ReponseWs create(String label, String description){
        CompanieType companieType = companieTypeRepository.findOneByLabel(label);
        if(companieType != null) return new ReponseWs("failed", "companieType exist", 408, null);
        companieType = new CompanieType();
        companieType.setLabel(label);
        companieType.setDescription(description);
        companieTypeRepository.save(companieType);
        return new ReponseWs("success", "create", 200, companieType.getLabel());
    }

    public ReponseWs update(Integer id, String label, String description){
        CompanieType companieType = companieTypeRepository.findOneById(id);
        if(companieType == null) return new ReponseWs("failed", "companieType not found", 404, null);
        companieType.setLabel(label);
        companieType.setDescription(description);
        companieTypeRepository.save(companieType);
        return new ReponseWs("success", "update", 200, companieType.getLabel());
    }

    public ReponseWs delete(Integer id){
        CompanieType companieType = companieTypeRepository.findOneById(id);
        if(companieType == null) return new ReponseWs("failed", "companieType not found", 404, null);
        companieTypeRepository.delete(companieType);
        return new ReponseWs("success", "delete", 200, null);
    }

    public ReponseWs list(Integer page, Integer size){
        Gson gson = new Gson();
        List<CompanieType> companieTypes = companieTypeRepository.findAll();
        List<CompanieTypeWs> companieTypeWs = companieTypes.stream().map(co -> gson.fromJson(gson.toJson(co), CompanieTypeWs.class)).collect(Collectors.toList());
        return new ReponseWs("success", "list", 200, companieTypeWs);
    }

    public ReponseWs find(Integer id){
        Gson gson = new Gson();
        CompanieType companieType = companieTypeRepository.findOneById(id);
        if(companieType == null) return new ReponseWs("failed", "companieType not found", 404, null);
        CompanieTypeWs companieTypeWs = gson.fromJson(gson.toJson(companieType), CompanieTypeWs.class);
        return new ReponseWs("success", "find", 200, companieTypeWs);
    }
}
