package com.kadod.fingerprint.controllers.web;

import com.kadod.commons.ws.CompanieWs;
import com.kadod.commons.ws.ReponseWs;
import com.kadod.fingerprint.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("web/api/v1.0/subscription")
public class SubscriptionController {

    @Autowired
    SubscriptionService subscriptionService;

    @PostMapping("/create")
    public ResponseEntity<ReponseWs> create(@RequestBody CompanieWs companieWs){
        ReponseWs reponseWs = this.subscriptionService.save(companieWs);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @PutMapping("/update")
    public ResponseEntity<ReponseWs> update(@RequestBody CompanieWs companieWs){
        ReponseWs reponseWs = this.subscriptionService.update(companieWs);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ReponseWs> find(@PathVariable Integer id){
        ReponseWs reponseWs = this.subscriptionService.findCompany(id);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/list")
    public ResponseEntity<ReponseWs> list(@RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        ReponseWs reponseWs = this.subscriptionService.listCompany(page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }




}
