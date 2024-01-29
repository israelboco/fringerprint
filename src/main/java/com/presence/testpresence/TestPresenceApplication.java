package com.presence.testpresence;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {
		"com.presence.testpresence.*"
})
@Configuration
@EnableAsync
@EntityScan(basePackages = {"com.presence.testpresence.model.entities"})
@EnableJpaRepositories(basePackages = {"com.presence.testpresence.model.repositories"})
public class TestPresenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestPresenceApplication.class, args);
	}

}
