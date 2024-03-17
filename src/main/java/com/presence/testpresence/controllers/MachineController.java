package com.presence.testpresence.controllers;

import com.presence.testpresence.services.MachineService;
import com.presence.testpresence.ws.MachineWs;
import com.presence.testpresence.ws.ReponseWs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1.0/machine")
public class MachineController {

    @Autowired
    MachineService machineService;

    @PostMapping("/save")
    public ResponseEntity<ReponseWs> createMachine(@RequestBody MachineWs ws){
        ReponseWs reponseWs = machineService.saveMachine(ws);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @PutMapping("/update")
    public ResponseEntity<ReponseWs> updateMachine(@RequestBody MachineWs ws){
        ReponseWs reponseWs = machineService.updateMachine(ws);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @PutMapping("/active")
    public ResponseEntity<ReponseWs> activeMachine(@RequestParam Integer idMachine, @RequestParam Boolean active){
        ReponseWs reponseWs = machineService.activeMachine(idMachine, active);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ReponseWs> delete(@RequestParam Integer idMachine){
        ReponseWs reponseWs = machineService.deleteMachine(idMachine);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/find")
    public ResponseEntity<ReponseWs> find(@RequestParam Integer idMachine){
        ReponseWs reponseWs = machineService.findMachine(idMachine);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ReponseWs> findMachine(@PathVariable Integer id){
        ReponseWs reponseWs = this.machineService.findMachine(id);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/list-of-compagnie")
    public ResponseEntity<ReponseWs> listOfCompany(@RequestParam Integer idCompany){
        ReponseWs reponseWs = machineService.listMachineOfCompanie(idCompany);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/connect/compagnie")
    public ResponseEntity<ReponseWs> connect(@RequestParam Integer idMachine, @RequestParam Integer idCompany){
        ReponseWs reponseWs = machineService.connect(idMachine, idCompany);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("active/list-of-compagnie")
    public ResponseEntity<ReponseWs> listOfCompanyActive(@RequestParam Integer idMachine, @RequestParam Boolean active){
        ReponseWs reponseWs = machineService.listActiveOfCompanie(idMachine, active);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("active/list")
    public ResponseEntity<ReponseWs> listActive(@RequestParam Boolean active){
        ReponseWs reponseWs = machineService.listActive(active);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }


}
