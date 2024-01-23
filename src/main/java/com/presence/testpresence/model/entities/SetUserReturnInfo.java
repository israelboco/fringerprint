package com.presence.testpresence.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "set_user_return_info")
public class SetUserReturnInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ret")
	private String ret;
    @Column(name = "sn")
	private String sn;
    @Column(name = "result")
	private Boolean result;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public Boolean getResult() {
		return result;
	}
	public void setResult(Boolean result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "SetUserReturnInfo [ret=" + ret + ", sn=" + sn + ", result="
				+ result + "]";
	}



}
