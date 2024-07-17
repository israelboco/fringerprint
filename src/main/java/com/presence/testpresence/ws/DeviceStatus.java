package com.presence.testpresence.ws;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.Queue;

public class DeviceStatus {

	private String deviceSn;
	private org.java_websocket.WebSocket webSocket;
	private int status;

	public org.java_websocket.WebSocket getWebSocket() {
		return webSocket;
	}

	public void setWebSocket(org.java_websocket.WebSocket webSocket) {
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
