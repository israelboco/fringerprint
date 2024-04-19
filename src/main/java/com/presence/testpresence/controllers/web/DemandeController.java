package com.presence.testpresence.controllers.web;

import com.presence.testpresence.services.DemandeService;
import com.presence.testpresence.ws.DemandeWs;
import com.presence.testpresence.ws.ReponseWs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("web/api/v1.0/demande")
public class DemandeController {

    @Autowired
    DemandeService demandeService;

    @PostMapping("/accept")
    public ResponseEntity<ReponseWs> accept(@RequestHeader("access-token") String access_token, @RequestBody DemandeWs demandeWs){
        ReponseWs reponseWs = this.demandeService.accept(access_token, demandeWs);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }
    @PostMapping("/accept/admin")
    public ResponseEntity<ReponseWs> acceptAdmin(@RequestBody DemandeWs demandeWs){
        ReponseWs reponseWs = this.demandeService.acceptAdmin(demandeWs);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @PostMapping("/refuse")
    public ResponseEntity<ReponseWs> refuse(@RequestHeader("access_token") String access_token, @RequestBody DemandeWs demandeWs){
        ReponseWs reponseWs = this.demandeService.refuse(access_token, demandeWs);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/list/accepter")
    public ResponseEntity<ReponseWs> listAccepter(@RequestHeader("access_token") String access_token, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        ReponseWs reponseWs = this.demandeService.listAccept(access_token, page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/list/employee")
    public ResponseEntity<ReponseWs> list(@RequestHeader("access_token") String access_token, @RequestParam(required = false) String date, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        ReponseWs reponseWs = this.demandeService.list(access_token, date, page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/list/resufer")
    public ResponseEntity<ReponseWs> listrefuser(@RequestHeader("access_token") String access_token, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        ReponseWs reponseWs = this.demandeService.listRefuser(access_token, page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/list")
    public ResponseEntity<ReponseWs> listDemande(@RequestHeader("access_token") String access_token, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        ReponseWs reponseWs = this.demandeService.listDemande(access_token, page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

}
