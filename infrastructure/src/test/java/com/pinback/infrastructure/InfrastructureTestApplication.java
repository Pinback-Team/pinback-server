package com.pinback.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.pinback")
@EntityScan("com.pinback.domain")
@EnableJpaRepositories("com.pinback.infrastructure")
public class InfrastructureTestApplication {
	public static void main(String[] args) {
		SpringApplication.run(InfrastructureTestApplication.class, args);
	}
}
