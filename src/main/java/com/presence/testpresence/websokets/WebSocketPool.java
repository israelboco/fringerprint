package com.presence.testpresence.websokets;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.presence.testpresence.ws.DeviceStatus;
import org.springframework.web.socket.WebSocketSession;
//import org.java_websocket.WebSocket;


public class WebSocketPool {
	
	
	public static  final Map<String, DeviceStatus> wsDevice = new HashMap<String, DeviceStatus>();




	/* Socket avec état */
    public static WebSocketSession getDeviceSocketBySn(String deviceSn) {
    	DeviceStatus deviceStatus = wsDevice.get(deviceSn);
    
		return deviceStatus.getWebSocket();
		
	}


	/* Ajouter une connexion avec le statut
    */
    
    public static void addDeviceAndStatus(String deviceSn,DeviceStatus deviceStatus) {
		wsDevice.put(deviceSn, deviceStatus);
	}


	/* Envoi de données à un seul utilisateur avec statut */
    public static void sendMessageToDeviceStatus(String sn,String message) {
		DeviceStatus deviceStatus=wsDevice.get(sn);
		WebSocketSession conn=deviceStatus.getWebSocket();
			if(null!=conn){				
				conn.send(message);	
			}			
	}


	/* delete l'equipement avec le statut */
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

	/* Status du device */
  public static DeviceStatus getDeviceStatus(String sn) {
	//  System.out.println(wsDevice.get);
	return wsDevice.get(sn);
	
}
    
    
    
    
   /* 发送*/
    public static void sendMessageToAllDeviceFree(String message) {
	   System.out.println("空闲发送数据");
	   Collection<DeviceStatus>deviceStatus=wsDevice.values();
	   
	   synchronized (deviceStatus) {
		for (DeviceStatus deviceStatus2:deviceStatus) {
			if (deviceStatus2.getWebSocket()!=null) {
				deviceStatus2.getWebSocket().send(message);
			}
		}
	}
	}
    
    
    
}
