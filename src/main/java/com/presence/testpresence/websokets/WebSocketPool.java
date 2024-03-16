package com.presence.testpresence.websokets;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.presence.testpresence.ws.DeviceStatus;
import org.java_websocket.WebSocket;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;


public class WebSocketPool {
	
	
	public static  final Map<String, DeviceStatus>wsDevice=new HashMap<String, DeviceStatus>();
	
	
    
  
  /*  带状态的socket*/
    public static WebSocketSession getDeviceSocketBySn(String deviceSn) {
    	DeviceStatus deviceStatus=wsDevice.get(deviceSn);
    
		return deviceStatus.getWebSocket();
		
	}
    
   
    /*添加含状态的连接
    */
    
    public static void addDeviceAndStatus(String deviceSn,DeviceStatus deviceStatus) {
		wsDevice.put(deviceSn, deviceStatus);
	}
       
 
  /*  向带状态的用户单个用户发送数据*/
    public static void sendMessageToDeviceStatus(String sn,String message) throws IOException {
		DeviceStatus deviceStatus=wsDevice.get(sn);
		WebSocketSession conn=deviceStatus.getWebSocket();
			if(null!=conn){				
				conn.sendMessage(new TextMessage(message));
			}			
	}
    

   /*移除带状态的设备*/
    public static boolean removeDeviceStatus(String sn) {
		if(wsDevice.containsKey(sn)){
			wsDevice.remove(sn);
			return true;
		}else{
			return false;
		}
	}
    
    
    public static String removeDeviceByWebsocket(WebSocket webSocket) {
    	Iterator<Entry<String, DeviceStatus>> entrys = wsDevice.entrySet().iterator();
		
    	while (entrys.hasNext()) {
			
    		Entry<String,DeviceStatus>entry=entrys.next();
    		if (entry.getValue().getWebSocket()==webSocket) {
				entrys.remove();
				return entry.getValue().getDeviceSn();
			}
    		
		}
		return null;
		
	}
          
      public static String getSerialNumber(WebSocket webSocket) {
    	  Collection<DeviceStatus>deviceStatus=wsDevice.values();
      	for(DeviceStatus ds:deviceStatus){
      		if(ds.getWebSocket()==webSocket){
      			wsDevice.remove(ds.getDeviceSn());     			
      			return ds.getDeviceSn();
      		}
      	}
		return null;
	}      
    
    /*判断状态*/
  public static DeviceStatus getDeviceStatus(String sn) {
	//  System.out.println(wsDevice.get);
	return wsDevice.get(sn);
	
}
    

   /* 发送*/
    public static void sendMessageToAllDeviceFree(String message) throws IOException {
	   System.out.println("空闲发送数据");
	   Collection<DeviceStatus>deviceStatus=wsDevice.values();
	   
	   synchronized (deviceStatus) {
		for (DeviceStatus deviceStatus2:deviceStatus) {
			if (deviceStatus2.getWebSocket()!=null) {
				deviceStatus2.getWebSocket().sendMessage(new TextMessage(message));
			}
		}
	}
	}
    
    
    
}
