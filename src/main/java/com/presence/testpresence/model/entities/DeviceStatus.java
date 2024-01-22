package com.presence.testpresence.model.entities;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.Queue;

//import org.java_websocket.WebSocket;
@Entity
@Table(name = "device_status")
public class DeviceStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@JoinColumn(name = "device_sn", referencedColumnName = "id")
	@ManyToOne
	private Device deviceSn;
	//private  WebSocket webSocket;
	@Column(name = "status")
	private int status;

	
//	public WebSocket getWebSocket() {
//		return webSocket;
//	}
//	public void setWebSocket(WebSocket webSocket) {
//		this.webSocket = webSocket;
//	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Device getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(Device deviceSn) {
		this.deviceSn = deviceSn;
	}

	@Override
	public String toString() {
		return "DeviceStatus [deviceSn=" + deviceSn + ", webSocket="
//				+ webSocket +
	+			", status=" + status + "]";
	}
	
	

}
