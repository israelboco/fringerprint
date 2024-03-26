package com.presence.testpresence.controllers;

import com.presence.testpresence.services.TypeMachineService;
import com.presence.testpresence.ws.ReponseWs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1.0/typemachine")
public class TypeMachineController {

    @Autowired
    TypeMachineService typeMachineService;

    @PostMapping("/create")
    public ResponseEntity<ReponseWs> create(@RequestParam String label, @RequestParam String description){
        ReponseWs reponseWs = typeMachineService.create(label, description);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @PutMapping("/update")
    public ResponseEntity<ReponseWs> update(@RequestParam Integer id, @RequestParam String label, @RequestParam String description){
        ReponseWs reponseWs = typeMachineService.update(id, label, description);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ReponseWs> delete(@RequestParam Integer id){
        ReponseWs reponseWs = typeMachineService.delete(id);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/list")
    public ResponseEntity<ReponseWs> list(@RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        ReponseWs reponseWs = typeMachineService.list(page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/find")
    public ResponseEntity<ReponseWs> find(@RequestParam Integer id){
        ReponseWs reponseWs = typeMachineService.find(id);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }


}
