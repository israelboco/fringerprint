package com.presence.testpresence.controllers;

import com.presence.testpresence.services.CompanieService;
import com.presence.testpresence.ws.CompanieWs;
import com.presence.testpresence.ws.ReponseWs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1.0/companies")
public class CompanieController {

    @Autowired
    CompanieService companieService;

    @PostMapping("/create")
    public ResponseEntity<ReponseWs> create(@RequestBody CompanieWs companieWs){
        ReponseWs reponseWs = this.companieService.save(companieWs);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @PutMapping("/update")
    public ResponseEntity<ReponseWs> update(@RequestBody CompanieWs companieWs){
        ReponseWs reponseWs = this.companieService.update(companieWs);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/list")
    public ResponseEntity<ReponseWs> list(@RequestParam Integer page, @RequestParam Integer size){
        ReponseWs reponseWs = this.companieService.listCompany(page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }


}
