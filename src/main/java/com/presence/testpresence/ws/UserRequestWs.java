package com.presence.testpresence.ws;

import com.presence.testpresence.model.entities.Role;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserRequestWs {

    private Integer id;
    private String nom;
    private String prenom;
    private String email;
    private String company;
    private String password;
    private Integer enrollId;
    private List<Integer> idRoles;

}
