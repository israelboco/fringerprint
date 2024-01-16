package com.presence.testpresence.services;

import com.google.gson.Gson;
import com.presence.testpresence.model.entities.Companie;
import com.presence.testpresence.model.entities.Machine;
import com.presence.testpresence.model.repositories.MachineRepository;
import com.presence.testpresence.ws.MachineWs;
import com.presence.testpresence.ws.ReponseWs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MachineService {


    @Autowired
    MachineRepository machineRepository;

    public ReponseWs saveMachine(MachineWs ws){
        Gson gson = new Gson();
        Machine machine = gson.fromJson(gson.toJson(ws), Machine.class);
        machineRepository.save(machine);
        return new ReponseWs("success", "create", 200, ws);
    }

    public ReponseWs updateMachine(MachineWs ws){
        Gson gson = new Gson();
        Machine machine = gson.fromJson(gson.toJson(ws), Machine.class);
        machineRepository.save(machine);
        return new ReponseWs("success", "update", 200, ws);
    }

    public ReponseWs findMachine(Integer id){
        Gson gson = new Gson();
        Machine machine = machineRepository.findOneById(id);
        if (machine == null) return new ReponseWs("failed", "machine not found", 200, null);
        MachineWs machineWs = gson.fromJson(gson.toJson(id), MachineWs.class);
        machineRepository.save(machine);
        return new ReponseWs("success", "find", 200, machineWs);
    }

    public ReponseWs deleteMachine(Integer id){
        Machine machine = machineRepository.findOneById(id);
        if (machine == null) return new ReponseWs("failed", "machine not found", 404, null);
        machineRepository.delete(machine);
        return new ReponseWs("success", "delete", 200, null);
    }

    public ReponseWs listActive(Boolean active){
        Gson gson = new Gson();
        List<Machine> machines = machineRepository.findByActive(active);
        List<MachineWs> machinesWs = machines.stream().map(m -> gson.fromJson(gson.toJson(m), MachineWs.class)).toList();
        return new ReponseWs("success", "list", 200, machinesWs);
    }

    public ReponseWs listMachineOfCompanie(Companie companie){
        Gson gson = new Gson();
        List<Machine> machines = machineRepository.findByCompanie(companie);
        List<MachineWs> machinesWs = machines.stream().map(m -> gson.fromJson(gson.toJson(m), MachineWs.class)).toList();
        return new ReponseWs("success", "list", 200, machinesWs);
    }

    public ReponseWs listActiveOfCompanie( Companie companie, Boolean active){
        Gson gson = new Gson();
        List<Machine> machines = machineRepository.findByCompanieAndActive(companie, active);
        List<MachineWs> machinesWs = machines.stream().map(m -> gson.fromJson(gson.toJson(m), MachineWs.class)).toList();
        return new ReponseWs("success", "list", 200, machinesWs);
    }

}
