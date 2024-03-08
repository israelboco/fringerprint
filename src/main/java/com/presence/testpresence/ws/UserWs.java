package com.presence.testpresence.ws;

import com.presence.testpresence.model.entities.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserWs {

    private Integer id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private Integer enrollId;
    private Set<Role> roles;

}
