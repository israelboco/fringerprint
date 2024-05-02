package com.presence.testpresence.model.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "row_conversations")
public class RowConversation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "create_by_id", referencedColumnName = "id")
    @ManyToOne
    private Employee createBy;
    @JoinColumn(name = "create_to_id", referencedColumnName = "id")
    @ManyToOne
    private Employee createTo;
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Employee createBy) {
        this.createBy = createBy;
    }

    public Employee getCreateTo() {
        return createTo;
    }

    public void setCreateTo(Employee createTo) {
        this.createTo = createTo;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
