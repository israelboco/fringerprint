package com.presence.testpresence.controllers;

import com.presence.testpresence.services.DemandeService;
import com.presence.testpresence.ws.DemandeWs;
import com.presence.testpresence.ws.ReponseWs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1.0/demande")
public class DemandeController {

    @Autowired
    DemandeService demandeService;

    @PostMapping("/accept")
    public ResponseEntity<ReponseWs> accept(@RequestParam String token, @RequestBody DemandeWs demandeWs){
        ReponseWs reponseWs = this.demandeService.accept(token, demandeWs);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }
    @PostMapping("/accept/admin")
    public ResponseEntity<ReponseWs> acceptAdmin(@RequestBody DemandeWs demandeWs){
        ReponseWs reponseWs = this.demandeService.acceptAdmin(demandeWs);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @PostMapping("/refuse")
    public ResponseEntity<ReponseWs> refuse(@RequestParam String token, @RequestBody DemandeWs demandeWs){
        ReponseWs reponseWs = this.demandeService.refuse(token, demandeWs);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/list/accepter")
    public ResponseEntity<ReponseWs> listAccepter(@RequestParam String token, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        ReponseWs reponseWs = this.demandeService.listAccept(token, page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/list/resufer")
    public ResponseEntity<ReponseWs> listrefuser(@RequestParam String token, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        ReponseWs reponseWs = this.demandeService.listRefuser(token, page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

}
