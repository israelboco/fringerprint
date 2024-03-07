package com.presence.testpresence.config;

import com.presence.testpresence.websokets.WSServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

@Configuration
public class WebSocketConfig {

//    @Value("${server.hostname}")
//    private String hostname;

    @Bean
    public WSServer serverEndpointExporter(String[] args) throws IOException, InterruptedException {
        int port = 8887; // 843 flash policy port
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception ex) {
        }
        InetSocketAddress inetSocketAddress = new InetSocketAddress("https://is-fing.osc-fr1.scalingo.io", port);
        WSServer s = new WSServer(inetSocketAddress);
        s.start();
        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String in = sysin.readLine();
            s.broadcast(in);
            if (in.equals("exit")) {
                s.stop(1000);
                break;
            }
        }
        return s;
    }

}
