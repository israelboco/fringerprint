package com.presence.testpresence.websokets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

//import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class AddressIPServer {

    @Value("${server.address}")
    private String addressServer;

//    @Autowired
//    private HttpServletRequest request;


    public static Logger logger = LoggerFactory.getLogger(AddressIPServer.class);

    public InetSocketAddress addressIP (int port) {
        InetSocketAddress address = new InetSocketAddress(port);
        try{
            logger.info("addressServer: " + addressServer);
//            System.out.println(request.getRemoteHost());

            address = new InetSocketAddress(InetAddress.getByName(addressServer), port);
        } catch (Exception e1) {
            System.out.println("Échec du démarrage de webSocket sur le server de production !");
            e1.printStackTrace();
        }
        logger.info("address: " + address);
        return address;
    }
}
