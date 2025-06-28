package com.pinback.pinback_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PinbackServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PinbackServerApplication.class, args);
	}

}
