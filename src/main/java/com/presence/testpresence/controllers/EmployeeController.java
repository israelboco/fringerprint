package com.presence.testpresence.controllers;

import com.presence.testpresence.services.EmployeeService;
import com.presence.testpresence.ws.EmployeeWs;
import com.presence.testpresence.ws.ReponseWs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1.0/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/create")
    public ReponseWs create(@RequestBody EmployeeWs ws){
        return this.employeeService.saveEmployee(ws);
    }

    @PutMapping("/update")
    public ReponseWs update(@RequestBody EmployeeWs ws){
        return this.employeeService.updateEmployee(ws);
    }

    @GetMapping("/company/list")
    public ReponseWs listOfCompany(@RequestParam Integer idCompany, @RequestParam(required = false, defaultValue = "0") Integer page,
                                   @RequestParam(required = false, defaultValue = "10") Integer size
                                   ){
        return this.employeeService.listEmployeeOfCompany(idCompany, page, size);
    }

    @GetMapping("/list")
    public ReponseWs list(@RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "10") Integer size){
        return this.employeeService.listEmployee(page, size);

    }

}
