package com.presence.testpresence.controllers;

import com.presence.testpresence.services.UserService;
import com.presence.testpresence.ws.ReponseWs;
import com.presence.testpresence.ws.UserRequestWs;
import com.presence.testpresence.ws.UserWs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1.0/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ReponseWs login(@RequestParam String email, @RequestParam String password){
        return this.userService.login(email, password);
    }

    @PostMapping("/update")
    public ReponseWs update(@RequestBody UserRequestWs userWs){
        return this.userService.update(userWs);
    }

    @PostMapping("/register")
    public ReponseWs register(@RequestBody UserRequestWs ws){
        return this.userService.register(ws);
    }

    @GetMapping("/")
    public ReponseWs getUser(@RequestParam() String token){

        return this.userService.getUser(token);
    }
    @GetMapping("/list")
    public ReponseWs listUser(@RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        return this.userService.listUser(page, size);
    }


    @GetMapping("/token/refresh")
    public ReponseWs refeshToken(@RequestParam() String token){
        return this.userService.refeshToken(token);
    }

    @GetMapping("/find")
    public ResponseEntity<ReponseWs> find(@RequestParam Integer id){
        ReponseWs reponseWs = this.userService.find(id);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }
}
