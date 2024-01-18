package com.presence.testpresence.services;

import com.google.gson.Gson;
import com.presence.testpresence.model.entities.Companie;
import com.presence.testpresence.model.entities.Employee;
import com.presence.testpresence.model.repositories.EmployeeRepository;
import com.presence.testpresence.ws.EmployeeWs;
import com.presence.testpresence.ws.ReponseWs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeService {
    private static Logger logger = LogManager.getLogger(EmployeeService.class);

    @Autowired
    EmployeeRepository employeeRepository;

    public ReponseWs saveEmployee(EmployeeWs ws){
        Gson gson = new Gson();
        Employee employee = gson.fromJson(gson.toJson(ws), Employee.class);
        employeeRepository.save(employee);
        return new ReponseWs("success", "create", 200, ws);
    }

    public ReponseWs updateEmployee(EmployeeWs ws){
        Gson gson = new Gson();
        Employee employee = gson.fromJson(gson.toJson(ws), Employee.class);
        employeeRepository.save(employee);
        return new ReponseWs("success", "update", 200, ws);
    }

    public ReponseWs listEmployeeOfCompany(Companie companie){
        Gson gson = new Gson();
        List<Employee> employees = employeeRepository.findByCompanie(companie);
        List<EmployeeWs> employeesWs = employees.stream().map(m -> gson.fromJson(gson.toJson(m), EmployeeWs.class)).collect(Collectors.toList());
        return new ReponseWs("success", "list", 200, employeesWs);
    }

}
