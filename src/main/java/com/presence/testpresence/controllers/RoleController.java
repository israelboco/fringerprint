package com.presence.testpresence.controllers;

import com.presence.testpresence.services.RoleService;
import com.presence.testpresence.ws.ReponseWs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1.0/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<ReponseWs> create(@RequestParam String label, @RequestParam String description){
        ReponseWs reponseWs = roleService.create(label, description);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @PutMapping("/update")
    public ResponseEntity<ReponseWs> update(@RequestParam Integer id, @RequestParam String label, @RequestParam String description){
        ReponseWs reponseWs = roleService.update(id, label, description);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ReponseWs> delete(@RequestParam Integer id){
        ReponseWs reponseWs = roleService.delete(id);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/list")
    public ResponseEntity<ReponseWs> list(@RequestParam Integer page, @RequestParam Integer size){
        ReponseWs reponseWs = roleService.list(page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }


}
