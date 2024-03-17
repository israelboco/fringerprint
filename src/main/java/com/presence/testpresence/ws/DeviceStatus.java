package com.presence.testpresence.ws;

import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.Queue;

public class DeviceStatus {

	private String deviceSn;
	private WebSocketSession webSocket;
	private int status;

	public WebSocketSession getWebSocket() {
		return webSocket;
	}

	public void setWebSocket(WebSocketSession webSocket) {
		this.webSocket = webSocket;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	@Override
	public String toString() {
		return "DeviceStatus [deviceSn=" + deviceSn + ", webSocket="
//				+ webSocket +
	+			", status=" + status + "]";
	}
	
	

}
