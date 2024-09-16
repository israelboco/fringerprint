package com.kadod.fingerprint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.URISyntaxException;


@SpringBootApplication(scanBasePackages = {
		"com.kadod.fingerprint.*"
})
//@EnableWebSocket
@Configuration
@EnableAsync
@EnableScheduling
@EntityScan(basePackages = {"com.kadod.database.model.entities"})
@EnableJpaRepositories(basePackages = {"com.kadod.database.model.repositories"})
public class FingerprintApplication {

	public static void main(String[] args) {
		SpringApplication.run(FingerprintApplication.class, args);
	}

}
