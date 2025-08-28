package com.pinback.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
	"com.pinback.api",
	"com.pinback.application",
	"com.pinback.infrastructure",
	"com.pinback.shared"
})
@EntityScan("com.pinback.domain")
@EnableJpaRepositories("com.pinback.infrastructure")
public class PinbackApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(PinbackApiApplication.class, args);
	}
}
