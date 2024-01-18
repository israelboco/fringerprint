package com.presence.testpresence.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "recordsLog")
public class recordsLog<T> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "cmd")
	String cmd;
	@Column(name = "serialNum")
	String serialNum;
	@Column(name = "count")
	int count;
	@Column(name = "longdex")
	int longdex;
	@Column(name = "data")
	private T data;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getLongdex() {
		return longdex;
	}
	public void setLongdex(int longdex) {
		this.longdex = longdex;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "recordsLog [cmd=" + cmd + ", serialNum=" + serialNum
				+ ", count=" + count + ", longdex=" + longdex + ", data="
				+ data + "]";
	}
	
    
}
