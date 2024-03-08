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

    @Column(name = "date_expire_token")
    private Date date_expire_token;
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

    public Date getDate_expire_token() {
        return date_expire_token;
    }

    public void setDate_expire_token(Date date_expire_token) {
        this.date_expire_token = date_expire_token;
    }
}
