package com.kadod.fingerprint.controllers;

import com.kadod.fingerprint.services.CompanieService;
import com.kadod.fingerprint.ws.CompanieWs;
import com.kadod.fingerprint.ws.ReponseWs;
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

    @GetMapping("/find/{id}")
    public ResponseEntity<ReponseWs> find(@PathVariable Integer id){
        ReponseWs reponseWs = this.companieService.findCompany(id);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/list")
    public ResponseEntity<ReponseWs> list(@RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        ReponseWs reponseWs = this.companieService.listCompany(page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }




}
