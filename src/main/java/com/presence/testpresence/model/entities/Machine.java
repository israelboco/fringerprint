package com.presence.testpresence.model.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "machines")
public class Machine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "serial_no")
    private String serialNo;
    @JoinColumn(name = "type_machine_id", referencedColumnName = "id")
    @ManyToOne
    private TypeMachine typeMachine;
    @Column(name = "name")
    private String name;
    @Column(name = "adresse_mac")
    private String AdressMac;
    @Column(name = "adresse_ip")
    private String AdresseIp;
    @JoinColumn(name = "companies_type_id", referencedColumnName = "id")
    @ManyToOne
    private Companie Companie;
    @Column(name = "active")
    private String active;
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdressMac() {
        return AdressMac;
    }

    public void setAdressMac(String adressMac) {
        AdressMac = adressMac;
    }

    public com.presence.testpresence.model.entities.Companie getCompanie() {
        return Companie;
    }

    public void setCompanie(com.presence.testpresence.model.entities.Companie companie) {
        Companie = companie;
    }

    public String getAdresseIp() {
        return AdresseIp;
    }

    public void setAdresseIp(String adresseIp) {
        AdresseIp = adresseIp;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public TypeMachine getTypeMachine() {
        return typeMachine;
    }

    public void setTypeMachine(TypeMachine typeMachine) {
        this.typeMachine = typeMachine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
