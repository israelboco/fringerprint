package com.kadod.fingerprint.controllers;

import com.kadod.commons.ws.ConversationRequestWs;
import com.kadod.commons.ws.ReponseWs;
import com.kadod.fingerprint.services.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1.0/conversation")
public class ConversationController {

    @Autowired
    ConversationService conversationService;


    @PostMapping("/sender")
    public ResponseEntity<ReponseWs> sender(@RequestBody ConversationRequestWs ws){
        ReponseWs reponseWs = this.conversationService.sender(ws);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @PostMapping("/senderWithAdmin")
    public ResponseEntity<ReponseWs> senderWithAdmin(@RequestBody ConversationRequestWs ws){
        ReponseWs reponseWs = this.conversationService.senderWithAdmin(ws);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/receive")
    public ResponseEntity<ReponseWs> receive(@RequestParam String token, @RequestParam Integer employeeId, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "10") Integer size){
        ReponseWs reponseWs = this.conversationService.receive(token, employeeId, page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }
    @GetMapping("/list/receive")
    public ResponseEntity<ReponseWs> listReceive(@RequestParam String token, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "10") Integer size){
        ReponseWs reponseWs = this.conversationService.listReceive(token, page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }


}
