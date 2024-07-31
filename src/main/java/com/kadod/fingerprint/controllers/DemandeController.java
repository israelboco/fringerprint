package com.kadod.fingerprint.controllers;

import com.kadod.fingerprint.services.DemandeService;
import com.kadod.fingerprint.ws.DemandeWs;
import com.kadod.fingerprint.ws.ReponseWs;
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

    @GetMapping("/list/employee")
    public ResponseEntity<ReponseWs> list(@RequestParam String token, @RequestParam(required = false) String date, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        ReponseWs reponseWs = this.demandeService.list(token, date, page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/list/resufer")
    public ResponseEntity<ReponseWs> listrefuser(@RequestParam String token, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        ReponseWs reponseWs = this.demandeService.listRefuser(token, page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/list")
    public ResponseEntity<ReponseWs> listDemande(@RequestParam String token, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        ReponseWs reponseWs = this.demandeService.listDemandeForCompanie(token, page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

}
