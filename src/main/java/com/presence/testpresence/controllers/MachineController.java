package com.presence.testpresence.controllers;

import com.presence.testpresence.services.MachineService;
import com.presence.testpresence.ws.MachineWs;
import com.presence.testpresence.ws.ReponseWs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1.0/machine")
public class MachineController {

    @Autowired
    MachineService machineService;

    @PostMapping("/save")
    public ResponseEntity<ReponseWs> createMachine(@RequestBody MachineWs ws){
        ReponseWs reponseWs = machineService.saveMachine(ws);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

}
