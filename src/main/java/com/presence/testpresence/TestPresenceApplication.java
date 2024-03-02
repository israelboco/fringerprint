package com.presence.testpresence;


import com.presence.testpresence.websokets.WSServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;

@SpringBootApplication(scanBasePackages = {
		"com.presence.testpresence.*"
})
@Configuration
@EnableAsync
@EntityScan(basePackages = {"com.presence.testpresence.model.entities"})
@EnableJpaRepositories(basePackages = {"com.presence.testpresence.model.repositories"})
public class TestPresenceApplication {

	public static void main(String[] args)  throws InterruptedException, IOException {
		SpringApplication.run(TestPresenceApplication.class, args);

//		int port = 8887; // 843 flash policy port
//		try {
//			port = Integer.parseInt(args[0]);
//		} catch (Exception ex) {
//		}
//		InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", port);
//		WSServer s = new WSServer(inetSocketAddress);
////		WSServer s = new WSServer(port);
//		s.start();
//		System.out.println("WSServer started on port: " + s.getPort());
//		System.out.println("WSServer started on address: " + s.getAddress());
//
//		BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
//		while (true) {
//			String in = sysin.readLine();
//			s.broadcast(in);
//			if (in.equals("exit")) {
//				s.stop(1000);
//				break;
//			}
//		}
	}
}
