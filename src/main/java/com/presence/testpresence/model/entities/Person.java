package com.presence.testpresence.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "rollId")
    private Integer rollId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getRollId() {
        return rollId;
    }

    public void setRollId(Integer rollId) {
        this.rollId = rollId;
    }

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", rollId=" + rollId
				+ "]";
	}


}