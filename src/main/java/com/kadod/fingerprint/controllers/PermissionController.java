package com.kadod.fingerprint.controllers;

import com.kadod.commons.ws.PermissionRequestWs;
import com.kadod.commons.ws.ReponseWs;
import com.kadod.fingerprint.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1.0/premission")
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    @PostMapping("/create")
    public ResponseEntity<ReponseWs> create(@RequestParam String token, @RequestBody PermissionRequestWs ws){
        ReponseWs reponseWs = this.permissionService.createPermission(token, ws);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }
    @PutMapping("/update")
    public ResponseEntity<ReponseWs> update(@RequestParam String token, @RequestBody PermissionRequestWs ws){
        ReponseWs reponseWs = this.permissionService.updatePermission(token, ws);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/find")
    public ResponseEntity<ReponseWs> find(@RequestParam String token, @RequestParam Integer permissionId){
        ReponseWs reponseWs = this.permissionService.findPermission(token, permissionId);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/employee/list")
    public ResponseEntity<ReponseWs> listEmployee(@RequestParam String token, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        ReponseWs reponseWs = this.permissionService.listEmployeePermissions(token, page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/list/all")
    public ResponseEntity<ReponseWs> listall( @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        ReponseWs reponseWs = this.permissionService.listAll(page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/admin/list/employee")
    public ResponseEntity<ReponseWs> list(@RequestParam String token, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        ReponseWs reponseWs = this.permissionService.listPermisssions(token, page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @GetMapping("/admin/list/employee/acceted")
    public ResponseEntity<ReponseWs> listAccepted(@RequestParam String token, @RequestParam Boolean accepted, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "25") Integer size){
        ReponseWs reponseWs = this.permissionService.listPermisssionAccepted(token, accepted, page, size);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

    @PostMapping("/accepted")
    public ResponseEntity<ReponseWs> listDemande(@RequestParam String token, @RequestParam() Integer permissionId, @RequestParam(required = false, defaultValue = "true") Boolean accepted){
        ReponseWs reponseWs = this.permissionService.acceptedPermission(token, permissionId, accepted);
        return new ResponseEntity<>(reponseWs, HttpStatus.ACCEPTED);
    }

}
