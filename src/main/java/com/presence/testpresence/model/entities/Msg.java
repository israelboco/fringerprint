package com.presence.testpresence.model.entities;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用的返回类
 * @author szw
 *
 */
@Entity
@Table(name = "msg")
public class Msg {

	//状态码 100-成功  200-失败
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "code")
	private int code;

	//提示信息
    @Column(name = "msg")
	private String msg;

	//用户要返回给浏览器的数据
    @Column(name = "extend")
	private Map<String, Object> extend = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getExtend() {
		return extend;
	}

	public void setExtend(Map<String, Object> extend) {
		this.extend = extend;
	}

	public static Msg success() {
		Msg result = new Msg();
		result.setCode(100);
		result.setMsg("处理成功！");
		return result;
	}

	public static Msg fail() {
		Msg result = new Msg();
		result.setCode(200);
		result.setMsg("处理失败！");
		return result;
	}

	public Msg add(String key,Object value) {
		this.getExtend().put(key, value);
		return this;
	}
}
