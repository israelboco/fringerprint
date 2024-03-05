package com.presence.testpresence.controllers;

import com.presence.testpresence.services.UserService;
import com.presence.testpresence.ws.ReponseWs;
import com.presence.testpresence.ws.UserWs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("v1.0/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ReponseWs login(@RequestParam String email, @RequestParam String password){
        return this.userService.login(email, password);
    }

    @PostMapping("/register")
    public ReponseWs register(@RequestBody UserWs ws){
        return this.userService.register(ws);
    }


}
