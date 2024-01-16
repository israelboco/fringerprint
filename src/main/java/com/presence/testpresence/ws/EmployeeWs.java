package com.presence.testpresence.ws;

import lombok.Data;

@Data
public class EmployeeWs {

    private Integer id;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String pin;
    private String salt;

}
