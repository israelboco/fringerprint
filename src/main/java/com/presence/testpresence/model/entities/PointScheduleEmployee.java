package com.presence.testpresence.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "point_schedule_employee")
public class PointScheduleEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "employees_id", referencedColumnName = "id")
    @ManyToOne
    private Employee employee;
}
