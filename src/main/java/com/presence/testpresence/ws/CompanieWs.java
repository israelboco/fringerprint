package com.presence.testpresence.ws;

import lombok.Data;

@Data
public class CompanieWs {

    private Integer id;
    private String nom;
    private String logo;
    private CompanieTypeWs type;


}
