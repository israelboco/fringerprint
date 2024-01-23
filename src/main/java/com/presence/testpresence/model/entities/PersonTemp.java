package com.presence.testpresence.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "person_temp")
public class PersonTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "userId")
	private int userId;
    @Column(name = "name")
	private String name;
    @Column(name = "privilege")
	private int privilege;
    @Column(name = "imagePath")
	private String imagePath;
    @Column(name = "password")
	private String password;
    @Column(name = "cardNum")
	private String cardNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrivilege() {
		return privilege;
	}
	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	@Override
	public String toString() {
		return "PersonTemp [userId=" + userId + ", name=" + name + ", privilege=" + privilege + ", imagePath="
				+ imagePath + ", password=" + password + ", cardNum=" + cardNum + "]";
	}


}
