package com.presence.testpresence;

import com.presence.testpresence.config.OpenApiConfig;
import com.presence.testpresence.controllers.device.AllController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
		"com.presence.testpresence."
})
@Configuration
@EnableAsync
@EnableScheduling
@EntityScan(basePackages = {"com.presence.testpresence.model.entities"})
@EnableJpaRepositories(basePackages = {"com.presence.testpresence.model.repositories"})
public class TestPresenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestPresenceApplication.class, args);
	}

}
