package com.presence.testpresence.websokets;

import org.java_websocket.WebSocket;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.net.UnknownHostException;

import static com.presence.testpresence.websokets.WSServer.l;

public class Text {

	public static void main(String[] args) throws InterruptedException{
	    System.out.println("Démarrage de webSocket");
	      //WebSocketImpl.DEBUG = false;
	      int port = 9000; // Définissez le port comme vous le souhaitez, tant qu'il ne duplique pas un port existant.
	      WSServer s =null;
	      try {
	          s = new WSServer(port);
	          s.start();
	        //  s.onOpen(conn, handshake);
	      } catch (UnknownHostException e1) {
	          System.out.println("Échec du démarrage de webSocket！");
	          e1.printStackTrace();
	      }
	    
	      System.out.println("Lancement réussi de webSocket！");
	      System.out.println("nombre de connexions"+l);
	//	final WebSocket websocket=
	//
		//org.java_websocket.WebSocket conn = null;
		
	//
		//System.out.println(new W);
	      
	 }

}
