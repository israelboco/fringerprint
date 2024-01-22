package com.presence.testpresence.services;

import com.google.gson.Gson;
import com.presence.testpresence.model.entities.Companie;
import com.presence.testpresence.model.entities.CompanieType;
import com.presence.testpresence.model.entities.Employee;
import com.presence.testpresence.model.repositories.CompanieRepository;
import com.presence.testpresence.model.repositories.CompanieTypeRepository;
import com.presence.testpresence.ws.CompanieWs;
import com.presence.testpresence.ws.EmployeeWs;
import com.presence.testpresence.ws.ReponseWs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanieService {

    private static Logger logger = LogManager.getLogger(CompanieService.class);

    @Autowired
    CompanieRepository companieRepository;
    @Autowired
    CompanieTypeRepository companieTypeRepository;

    public ReponseWs save(CompanieWs ws){
        CompanieType companieType = companieTypeRepository.findOneById(ws.getIdType());
        if (companieType == null) return new ReponseWs("failed", "type compagnie not found", 404, null);
        Gson gson = new Gson();
        Companie companie = gson.fromJson(gson.toJson(ws), Companie.class);
        companie.setType(companieType);
        companieRepository.save(companie);
        return new ReponseWs("success", "create", 200, ws);
    }

    public ReponseWs update(CompanieWs ws){
        Companie companie = companieRepository.findOneById(ws.getId());
        if (companie == null) return new ReponseWs("failed", "compagnie not found", 404, null);
        Gson gson = new Gson();
        CompanieType companieType = companieTypeRepository.findOneById(ws.getIdType());
        if(companieType == null) return new ReponseWs("failed", "companie type not found", 404, null);
        companie = gson.fromJson(gson.toJson(ws), Companie.class);
        companie.setType(companieType);
        companieRepository.save(companie);
        return new ReponseWs("success", "update", 200, ws);
    }

    public ReponseWs listCompany(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Gson gson = new Gson();
        List<Companie> companies = companieRepository.findAll();
        List<CompanieWs> companiesWs = companies.stream().map(c -> gson.fromJson(gson.toJson(c), CompanieWs.class)).collect(Collectors.toList());
        return new ReponseWs("success", "list", 200, companiesWs);
    }


}
