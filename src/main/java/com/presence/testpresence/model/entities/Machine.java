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
    private String adressMac;
    @Column(name = "adresse_ip")
    private String adresseIp;
    @JoinColumn(name = "companies_id", referencedColumnName = "id")
    @ManyToOne
    private Companie companie;
    @Column(name = "active")
    private Boolean active;
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
        return adressMac;
    }

    public void setAdressMac(String adressMac) {
        this.adressMac = adressMac;
    }

    public String getAdresseIp() {
        return adresseIp;
    }

    public void setAdresseIp(String adresseIp) {
        this.adresseIp = adresseIp;
    }

    public Companie getCompanie() {
        return companie;
    }

    public void setCompanie(Companie companie) {
        this.companie = companie;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
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
