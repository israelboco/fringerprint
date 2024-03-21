package com.presence.testpresence.services;

import com.google.gson.Gson;
import com.presence.testpresence.model.entities.*;
import com.presence.testpresence.model.enums.Constant;
import com.presence.testpresence.model.repositories.*;
import com.presence.testpresence.util.JwtUtil;
import com.presence.testpresence.ws.EmployeeWs;
import com.presence.testpresence.ws.ReponseWs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeService {
    private static Logger logger = LogManager.getLogger(EmployeeService.class);

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    CompanieRepository companieRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MachineRepository machineRepository;
    @Autowired
    EnrollInfoRepository enrollInfoRepository;
    @Autowired
    FileService fileService;

    public ReponseWs saveEmployee(EmployeeWs ws){
        Companie companie = companieRepository.findOneById(ws.getIdCompany());
        if (companie == null) return new ReponseWs(Constant.FAILED, "compagnie not found", 404, null);
        Gson gson = new Gson();
        User user = this.userRepository.findOneById(ws.getUser_id());
        if (user == null) return new ReponseWs(Constant.FAILED, "user not found", 404, null);
        Machine machine = machineRepository.findOneBySerialNo(ws.getDeviceSerial());
        if (machine == null) return new ReponseWs(Constant.FAILED, "device Serial not found", 404, null);
        EnrollInfo enrollInfo = this.enrollInfoRepository.findOneByIdAndMachine(ws.getEnrollId(), machine);
        if (enrollInfo == null) {
            enrollInfo = new EnrollInfo();
            enrollInfo.setEnrollId(ws.getEnrollId());
            enrollInfo.setMachine(machine);
            enrollInfoRepository.save(enrollInfo);
        }

        Employee employee = gson.fromJson(gson.toJson(ws), Employee.class);
        employee.setCompanie(companie);
        employee.setUser(user);
        employee.setEnrollInfo(enrollInfo);
        employee.setAdmin(ws.getIsAdmin());
        employeeRepository.save(employee);
        return new ReponseWs("success", "create", 200, ws);
    }

    public ReponseWs updateEmployee(String token, EmployeeWs ws){
        String email = JwtUtil.extractEmail(token);
        User user = userRepository.findOneByEmail(email);
        Employee employee = employeeRepository.findByUser(user);
        Companie companie = companieRepository.findOneById(employee.getCompanie().getId());
        if (companie == null) return new ReponseWs(Constant.FAILED, "compagnie not found", 404, null);
        Gson gson = new Gson();
        employee.setNom(ws.getNom());
        employee.setPrenom(ws.getPrenom());
        employee.setTelephone(ws.getTelephone());
        employee.setEmail(ws.getEmail());
        employee.setCompanie(companie);
        employeeRepository.save(employee);
        user.setNom(ws.getNom());
        user.setPrenom(ws.getPrenom());
        user.setEmail(ws.getEmail());
        userRepository.save(user);
        return new ReponseWs("success", "update", 200, ws);
    }

    public ReponseWs setProfile(String token, MultipartFile file){
        String email = JwtUtil.extractEmail(token);
        User user = userRepository.findOneByEmail(email);
        Employee employee = employeeRepository.findByUser(user);
        if (employee == null) return new ReponseWs(Constant.FAILED, "employer not found", 404, null);
        String profile = fileService.uploadFile(file);
        employee.setProfile(profile);
        employeeRepository.save(employee);
        return new ReponseWs("success", "profile", 200, null);
    }

    public ReponseWs listEmployeeOfCompany(Integer idCompanie, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Gson gson = new Gson();
        Companie companie = companieRepository.findOneById(idCompanie);
        if(companie == null) return new ReponseWs(Constant.FAILED, "company not found", 404, null);
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

    public ReponseWs find(Integer id){
        Gson gson = new Gson();
        Employee employee = employeeRepository.findOneById(id);
        if(employee == null) return new ReponseWs(Constant.FAILED, "employee not found", 404, null);
        EmployeeWs employeeWs = gson.fromJson(gson.toJson(employee), EmployeeWs.class);
        employeeWs.setCompany(employee.getCompanie().getNom());
        employeeWs.setIdCompany(employee.getCompanie().getId());
        employeeWs.setEnrollId(employee.getEnrollInfo().getEnrollId());
        employeeWs.setUser_id(employee.getUser().getId());
        return new ReponseWs("success", "find", 200, employeeWs);
    }

    public ReponseWs find(String token){
        Gson gson = new Gson();
        String email = JwtUtil.extractEmail(token);
        User user = userRepository.findOneByEmail(email);
        Employee employee = employeeRepository.findByUser(user);
        if(employee == null) return new ReponseWs(Constant.FAILED, "employee not found", 404, null);
        EmployeeWs employeeWs = gson.fromJson(gson.toJson(employee), EmployeeWs.class);
        employeeWs.setCompany(employee.getCompanie().getNom());
        employeeWs.setIdCompany(employee.getCompanie().getId());
        employeeWs.setEnrollId(employee.getEnrollInfo().getEnrollId());
        employeeWs.setUser_id(employee.getUser().getId());
        return new ReponseWs("success", "find", 200, employeeWs);
    }

}
