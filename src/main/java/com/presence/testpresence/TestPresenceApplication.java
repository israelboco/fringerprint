package com.presence.testpresence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication(scanBasePackages = {
		"com.presence.testpresence.*"
},
exclude = SecurityAutoConfiguration.class)
//@EnableWebSocket
@Configuration
@EnableAsync
@EnableScheduling
@EntityScan(basePackages = {"com.presence.testpresence.model.entities"})
@EnableJpaRepositories(basePackages = {"com.presence.testpresence.model.repositories"})
public class TestPresenceApplication {

	public static void main(String[] args) throws URISyntaxException {

		SpringApplication.run(TestPresenceApplication.class, args);

		ExampleClient c = new ExampleClient(new URI(
				"ws://192.168.137.1:7788")); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
		c.connect();
	}

}
