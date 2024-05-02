package com.presence.testpresence.controllers.web;

import com.presence.testpresence.services.PresenceService;
import com.presence.testpresence.ws.ReponseWs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("web/api/v1.0/presence")
public class WebPresenceController {

    @Autowired
    PresenceService presenceService;

    @PostMapping("/create")
    public ReponseWs create(@RequestParam String token){
        return this.presenceService.create(token);
    }

    @GetMapping("/list")
    public ReponseWs list(@RequestParam String token){
        return this.presenceService.list(token);
    }

    @GetMapping("/find")
    public ReponseWs find(@RequestParam String token, @RequestParam String date, @RequestParam(required = false) Integer userID){
        return this.presenceService.find(token, date, userID);
    }

    @GetMapping("/employee/presenceMonth")
    public ReponseWs presenceMonth(@RequestParam String token, @RequestParam String date, @RequestParam(required = false) Integer employeeID){
        return this.presenceService.presenceMonth(token, date, employeeID);
    }

    @GetMapping("list/presence/jour")
    public ReponseWs listPresenceMonth(@RequestParam String token, @RequestParam String date, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        return this.presenceService.listPresenceMonth(token, date, page, size);
    }
}
