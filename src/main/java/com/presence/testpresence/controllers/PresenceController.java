package com.presence.testpresence.controllers;

import com.presence.testpresence.services.PresenceService;
import com.presence.testpresence.ws.ReponseWs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1.0/presence")
public class PresenceController {

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
    public ReponseWs find(@RequestParam String token, @RequestParam String date){
        return this.presenceService.find(token, date);
    }

    @GetMapping("/presenceMonth")
    public ReponseWs presenceMonth(@RequestParam String token, @RequestParam String date){
        return this.presenceService.presenceMonth(token, date);
    }

}
