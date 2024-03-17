package com.presence.testpresence.ws;
import lombok.Data;
@Data
public class DemandeWs {
    private Integer userId;
    private Integer enrollId;
    private Integer companyID;
    private String deviceSerial;
    private Boolean status;
}
