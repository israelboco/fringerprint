package com.presence.testpresence.controllers;

import com.presence.testpresence.services.PresenceEmployeeService;
import com.presence.testpresence.ws.ReponseWs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("v1.0/presence")
public class PresenceController {

    @Autowired
    PresenceEmployeeService presenceService;

    @PostMapping("/create")
    public ReponseWs create(@RequestParam String token){
        return this.presenceService.create(token);
    }

    @GetMapping("/list")
    public ReponseWs list(@RequestParam String token){
        return this.presenceService.list(token);

    }

}
