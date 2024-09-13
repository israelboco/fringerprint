package com.kadod.fingerprint.services;

import com.google.gson.Gson;
import com.kadod.commons.enums.Constant;
import com.kadod.commons.ws.EmployeeWs;
import com.kadod.commons.ws.PermissionRequestWs;
import com.kadod.commons.ws.PermissionWs;
import com.kadod.commons.ws.ReponseWs;
import com.kadod.database.model.entities.Employee;
import com.kadod.database.model.entities.Permission;
import com.kadod.database.model.entities.User;
import com.kadod.database.model.enums.TypePermissionEnum;
import com.kadod.database.model.repositories.EmployeeRepository;
import com.kadod.database.model.repositories.PermissionRepository;
import com.kadod.database.model.repositories.UserRepository;
import com.kadod.fingerprint.util.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PermissionService {

    private static Logger logger = LogManager.getLogger(PermissionService.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    FileService fileService;


    public ReponseWs createPermission(String token, PermissionRequestWs ws){
        Gson gson = new Gson();
        String email = JwtUtil.extractEmail(token);
        User user = userRepository.findOneByEmail(email);
        if(user == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
        Employee employee = this.employeeRepository.findByUser(user);
        Permission permission = new Permission();
        permission.setAccepted(null);
        permission.setObjet(ws.getObjet());
        permission.setDescription(ws.getDescription());
        permission.setEmployee(employee);
        if(ws.getStartDateTimestamp() != null)
            permission.setStartDate(new Date(ws.getStartDateTimestamp()));
        if(ws.getEndDateTimestamp() != null)
            permission.setEndDate(new Date(ws.getEndDateTimestamp()));
//        permission.setType(gson.fromJson(gson.toJson(ws.getType()), TypePermissionEnum.class));
        String filename = this.fileService.imageUpload(ws.getPath());
        permission.setPath(filename);
        permission = permissionRepository.save(permission);
        PermissionWs permissionWs = gson.fromJson(gson.toJson(permission), PermissionWs.class);
        return new ReponseWs(Constant.SUCCESS, "permission create avec succés", 200, permissionWs);
    }

    public ReponseWs updatePermission(String token, PermissionRequestWs ws){
        Gson gson = new Gson();
        String email = JwtUtil.extractEmail(token);
        User user = userRepository.findOneByEmail(email);
        if(user == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
        Employee employee = this.employeeRepository.findByUser(user);
        Permission permission = this.permissionRepository.findOneById(ws.getId());
        if(permission == null)
            return new ReponseWs(Constant.FAILED, "permission not found", 404, null);
        permission.setAccepted(null);
        permission.setDescription(ws.getDescription());
        permission.setEmployee(employee);
        if(ws.getStartDateTimestamp() != null)
            permission.setStartDate(new Date(ws.getStartDateTimestamp()));
        if(ws.getEndDateTimestamp() != null)
            permission.setEndDate(new Date(ws.getEndDateTimestamp()));
        permission.setType(gson.fromJson(gson.toJson(ws.getType()), TypePermissionEnum.class));
        permissionRepository.save(permission);
        return new ReponseWs(Constant.SUCCESS, "permission create avec SUCCESS", 200, ws);

    }

    public ReponseWs findPermission(String token, Integer permissionId){
        Gson gson = new Gson();
        String email = JwtUtil.extractEmail(token);
        User user = userRepository.findOneByEmail(email);
        if(user == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
        Employee employee = this.employeeRepository.findByUser(user);
        Permission permission = this.permissionRepository.findOneById(permissionId);
        if(permission == null)
            return new ReponseWs(Constant.FAILED, "permission not found", 404, null);
        PermissionWs permissionWs = gson.fromJson(gson.toJson(permission), PermissionWs.class);
        permissionWs.setStartDateTimestamp(permission.getStartDate().getTime());
        permissionWs.setEndDateTimestamp(permission.getEndDate().getTime());
        permissionWs.setEmployeeWs(gson.fromJson(gson.toJson(permission.getEmployee()), EmployeeWs.class));
        return new ReponseWs(Constant.SUCCESS, "permission trouve avec SUCCESS", 200, permissionWs);
    }

    public ReponseWs acceptedPermission(String token, Integer permissionId, Boolean accepted){
        String emailAdmin = JwtUtil.extractEmail(token);
        User userAdmin = userRepository.findOneByEmail(emailAdmin);
        if(userAdmin == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
//        Employee employee = this.employeeRepository.findByUser(userAdmin);
        Permission permission = this.permissionRepository.findOneById(permissionId);
        if(permission == null) return new ReponseWs(Constant.FAILED, "permission not found", 404, null);
        permission.setAccepted(accepted);
        permissionRepository.save(permission);
        return new ReponseWs(Constant.SUCCESS, "permission accepter avec SUCCESS", 200, accepted);
    }

    public ReponseWs listEmployeePermissions(String token, Boolean accept, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        String email = JwtUtil.extractEmail(token);
        User user = userRepository.findOneByEmail(email);
        if(user == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
        Employee employee = employeeRepository.findByUser(user);
        if(employee == null) return new ReponseWs(Constant.FAILED, "employer invalide", 404, null);
        Page<Permission> pagePermissions = this.permissionRepository.findByEmployeeAndAccepted(employee, accept, pageable);
        List<PermissionWs> permissionWsList = pagePermissions.stream().map(this::getPermissionWs).collect(Collectors.toList());
        PageImpl<PermissionWs> permissionWsPage = new PageImpl<>(permissionWsList, pageable, pagePermissions.getTotalPages());
        return new ReponseWs(Constant.SUCCESS, "Listes de permissions de l'employees", 200, permissionWsPage);
    }

    public ReponseWs listPermisssions(String token, Boolean accept, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        String emailAdmin = JwtUtil.extractEmail(token);
        User userAdmin = userRepository.findOneByEmail(emailAdmin);
        if(userAdmin == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
        Employee employeeAdmin = employeeRepository.findByUser(userAdmin);
        if(employeeAdmin == null) return new ReponseWs(Constant.FAILED, "employer invalide", 404, null);
        List<Employee> employees = this.employeeRepository.findByEmployeeAdmin(employeeAdmin);
        Page<Permission> permissionPage = this.permissionRepository.findByEmployeeInAndAccepted(employees, accept, pageable);
        List<PermissionWs> permissionWsList = permissionPage.stream().map(this::getPermissionWs).collect(Collectors.toList());
        PageImpl<PermissionWs> permissionWsPage = new PageImpl<>(permissionWsList, pageable, permissionPage.getTotalPages());
        return new ReponseWs(Constant.SUCCESS, "Listes des permissions des employees de l'admin", 200, permissionWsPage);
    }

    public ReponseWs listPermisssionAccepted(String token, Boolean accepted, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        String emailAdmin = JwtUtil.extractEmail(token);
        User userAdmin = userRepository.findOneByEmail(emailAdmin);
        if(userAdmin == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
        Employee employeeAdmin = employeeRepository.findByUser(userAdmin);
        if(employeeAdmin == null) return new ReponseWs(Constant.FAILED, "employer invalide", 404, null);
        List<Employee> employees = this.employeeRepository.findByEmployeeAdmin(employeeAdmin);
        Page<Permission> permissionPage = this.permissionRepository.findByEmployeeInAndAccepted(employees, accepted, pageable);
        List<PermissionWs> permissionWsList = permissionPage.stream().map(this::getPermissionWs).collect(Collectors.toList());
        PageImpl<PermissionWs> permissionWsPage = new PageImpl<>(permissionWsList, pageable, permissionPage.getTotalPages());
        return new ReponseWs(Constant.SUCCESS, "Listes des permissions des employees de l'admin filtrer selon accepted", 200, permissionWsPage);
    }

    public ReponseWs listAll(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Permission> permissionPage = this.permissionRepository.findAll(pageable);
        List<PermissionWs> permissionWsList = permissionPage.stream().map(this::getPermissionWs).collect(Collectors.toList());
        PageImpl<PermissionWs> permissionWsPage = new PageImpl<>(permissionWsList, pageable, permissionPage.getTotalPages());
        return new ReponseWs(Constant.SUCCESS, "Listes de tout les permissions", 200, permissionWsPage);
    }

    private PermissionWs getPermissionWs(Permission permission){
        Gson gson = new Gson();
        PermissionWs permissionWs = gson.fromJson(gson.toJson(permission), PermissionWs.class);
        if(permission.getStartDate() != null)
            permissionWs.setStartDateTimestamp(permission.getStartDate().getTime());
        if(permission.getEndDate() != null)
            permissionWs.setEndDateTimestamp(permission.getEndDate().getTime());
        if (permission.getEmployee() != null)
            permissionWs.setEmployeeWs(this.getEmployeeWs(permission.getEmployee()));
        return permissionWs;
    }

    private EmployeeWs getEmployeeWs(Employee employee){
        Gson gson = new Gson();
        EmployeeWs employeeWs = gson.fromJson(gson.toJson(employee), EmployeeWs.class);
        employeeWs.setCompany(employee.getCompanie().getNom());
        employeeWs.setIdCompany(employee.getCompanie().getId());
        employeeWs.setEnrollId(employee.getEnrollInfo().getEnrollId());
        employeeWs.setUser_id(employee.getUser().getId());
        if(employee.getImageData() != null)
            employeeWs.setImageProfile(this.fileService.downloadImage(employee.getImageData()));
        return employeeWs;
    }
}
