package com.presence.testpresence.websokets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import static com.presence.testpresence.websokets.WSServer.l;

public class Text {

	@Value("${server.address}")
	private String addressServer;

	@Bean
	public void main(String[] args) throws InterruptedException{

		System.out.println("Démarrage de webSocket");
//	      WebSocketImpl.DEBUG = false;
		int port = 7788; // 端口随便设置，只要不跟现有端口重复就可以了
		WSServer s =null;
		try {
//			  s = new WSServer(port);
			String ipAddress = this.addressServer; // Add your own
			String hostName = ""; // Add your own


//			  InetSocketAddress byAddress1 = new InetSocketAddress(ipAddress, port);
//			  InetSocketAddress byAddress2 = new InetSocketAddress(InetAddress.getByName(ipAddress), port);
//
//			  InetSocketAddress byName1 = new InetSocketAddress(hostName, port);
//			  InetSocketAddress byName2 = new InetSocketAddress(InetAddress.getByName(hostName), port);
			s = new WSServer(new InetSocketAddress(InetAddress.getByName(ipAddress), port));
			s.start();
			//  s.onOpen(conn, handshake);
		} catch (UnknownHostException e1) {
			System.out.println("Échec du démarrage de webSocket !");
			e1.printStackTrace();
		}

		System.out.println("Le démarrage de webSocket a réussi !");
		System.out.println("nombre de connexions: " + l);
		System.out.println("Adress : " + s.getAddress());
//		final WebSocket websocket=

//		org.java_websocket.WebSocket conn = null;
//
//	//
//		System.out.println(new W);
	 }

}
