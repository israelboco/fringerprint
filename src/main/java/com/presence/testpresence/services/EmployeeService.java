package com.presence.testpresence.services;

import com.google.gson.Gson;
import com.presence.testpresence.model.entities.Companie;
import com.presence.testpresence.model.entities.Employee;
import com.presence.testpresence.model.repositories.CompanieRepository;
import com.presence.testpresence.model.repositories.EmployeeRepository;
import com.presence.testpresence.ws.EmployeeWs;
import com.presence.testpresence.ws.ReponseWs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeService {
    private static Logger logger = LogManager.getLogger(EmployeeService.class);

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CompanieRepository companieRepository;

    public ReponseWs saveEmployee(EmployeeWs ws){
        Companie companie = companieRepository.findOneById(ws.getIdCompany());
        if (companie == null) return new ReponseWs("failed", "compagnie not found", 404, null);
        Gson gson = new Gson();
        Employee employee = gson.fromJson(gson.toJson(ws), Employee.class);
        employee.setCompanie(companie);
        employeeRepository.save(employee);
        return new ReponseWs("success", "create", 200, ws);
    }

    public ReponseWs updateEmployee(EmployeeWs ws){
        Companie companie = companieRepository.findOneById(ws.getIdCompany());
        if (companie == null) return new ReponseWs("failed", "compagnie not found", 404, null);
        Gson gson = new Gson();
        Employee employee = employeeRepository.findOneById(ws.getId());
        if(employee == null) return new ReponseWs("failed", "employee not found", 404, null);
        employee = gson.fromJson(gson.toJson(ws), Employee.class);
        employee.setCompanie(companie);
        employeeRepository.save(employee);
        return new ReponseWs("success", "update", 200, ws);
    }

    public ReponseWs listEmployeeOfCompany(Integer idCompanie, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Gson gson = new Gson();
        Companie companie = companieRepository.findOneById(idCompanie);
        if(companie == null) return new ReponseWs("failed", "company not found", 404, null);
        List<Employee> employees = employeeRepository.findByCompanie(companie);
        List<EmployeeWs> employeesWs = employees.stream().map(m -> gson.fromJson(gson.toJson(m), EmployeeWs.class)).collect(Collectors.toList());
        return new ReponseWs("success", "list", 200, employeesWs);
    }

    public ReponseWs listEmployee(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Gson gson = new Gson();
        Page<Employee> employees = employeeRepository.findAll(pageable);
        List<EmployeeWs> employeesWs = employees.getContent().stream().map(m -> gson.fromJson(gson.toJson(m), EmployeeWs.class)).collect(Collectors.toList());
        return new ReponseWs("success", "list", 200, employeesWs);
    }

}
