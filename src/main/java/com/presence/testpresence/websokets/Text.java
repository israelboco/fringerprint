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

	public static WSServer getWsServer(int port) {
		AddressIPServer address = new AddressIPServer(); // Add your own
		String hostName = ""; // Add your own


//			  InetSocketAddress byAddress1 = new InetSocketAddress(ipAddress, port);
//			  InetSocketAddress byAddress2 = new InetSocketAddress(InetAddress.getByName(ipAddress), port);
//
//			  InetSocketAddress byName1 = new InetSocketAddress(hostName, port);
//			  InetSocketAddress byName2 = new InetSocketAddress(InetAddress.getByName(hostName), port);
		WSServer s = new WSServer(address.addressIP(port));
		s.start();
		System.out.println("Le démarrage de webSocket a réussi !");
		System.out.println("nombre de connexions: " + l);
		System.out.println("Adress : " + s.getAddress());
		return s;
	}

}
