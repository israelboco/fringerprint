package com.presence.testpresence.controllers;

import com.presence.testpresence.services.ConversationService;
import com.presence.testpresence.ws.ConversationWs;
import com.presence.testpresence.ws.ReponseWs;
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
    public ResponseEntity<ReponseWs> sender(@RequestBody ConversationWs ws){
        ReponseWs reponseWs = this.conversationService.sender(ws);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/receive")
    public ResponseEntity<ReponseWs> receive(@RequestParam String token, @RequestParam Integer employeeId, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        ReponseWs reponseWs = this.conversationService.receive(token, employeeId);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }
    @GetMapping("/list/receive")
    public ResponseEntity<ReponseWs> listReceive(@RequestParam String token, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        ReponseWs reponseWs = this.conversationService.listReceive(token, page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }


}
