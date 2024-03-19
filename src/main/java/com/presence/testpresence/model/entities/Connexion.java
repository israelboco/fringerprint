package com.presence.testpresence.model.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "connexion")
public class Connexion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    @ManyToOne
    private User user;
    @Column(name = "token")
    private String token;
    @Column(name = "company")
    private String company;
    @Column(name = "date_expire_token")
    private Date dateExpireToken;
    @Column(name = "active")
    private Boolean active;
    @Column(name = "confirm_demande")
    private Boolean confirmDemande;
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getDateExpireToken() {
        return dateExpireToken;
    }

    public void setDateExpireToken(Date dateExpireToken) {
        this.dateExpireToken = dateExpireToken;
    }

    public Boolean getConfirmDemande() {
        return confirmDemande;
    }

    public void setConfirmDemande(Boolean confirmDemande) {
        this.confirmDemande = confirmDemande;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
