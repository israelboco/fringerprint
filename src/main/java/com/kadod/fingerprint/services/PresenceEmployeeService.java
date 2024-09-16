package com.kadod.fingerprint.services;

import com.google.gson.Gson;
import com.kadod.commons.ws.EmployeeWs;
import com.kadod.commons.ws.PresenceEmployeeWs;
import com.kadod.commons.ws.ReponseWs;
import com.kadod.database.model.entities.Employee;
import com.kadod.database.model.entities.PresenceEmployee;
import com.kadod.database.model.repositories.CompanieRepository;
import com.kadod.database.model.repositories.EmployeeRepository;
import com.kadod.database.model.repositories.PresenceEmployeeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PresenceEmployeeService {

    private static Logger logger = LogManager.getLogger(PresenceEmployeeService.class);

    @Autowired
    PresenceEmployeeRepository presenceEmployeeRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    CompanieRepository companieRepository;

    public ReponseWs savePresence(PresenceEmployeeWs ws){
        Employee employee = employeeRepository.findOneById(ws.getEmployeeId());
        if (employee == null) return new ReponseWs("failed", "employee not found", 404, null);
        Gson gson = new Gson();
        PresenceEmployee presenceEmployee = this.create(ws, employee);
        presenceEmployeeRepository.save(presenceEmployee);
        PresenceEmployeeWs presenceEmployeeWs = gson.fromJson(gson.toJson(presenceEmployee), PresenceEmployeeWs.class);
        presenceEmployeeWs.setEmployeeId(employee.getId());
        return new ReponseWs("success", "save presence", 200, presenceEmployeeWs);
    }

    private PresenceEmployee create(PresenceEmployeeWs ws, Employee employee){
        Gson gson = new Gson();
        Date created = new Date();
        PresenceEmployee presenceEmployee = gson.fromJson(gson.toJson(ws), PresenceEmployee.class);
        presenceEmployee.setEmployee(employee);
        presenceEmployee.setCreated(created);
        return presenceEmployee;
    }

    public ReponseWs updateEmployee(EmployeeWs ws){
        Gson gson = new Gson();
        Employee employee = employeeRepository.findOneById(ws.getId());
        if(employee == null) return new ReponseWs("failed", "employee not found", 404, null);
        employee = gson.fromJson(gson.toJson(ws), Employee.class);
        employeeRepository.save(employee);
        return new ReponseWs("success", "update", 200, ws);
    }

}
