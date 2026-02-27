package com.pinback.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.pinback.application.config.JobImageConfig;
import com.pinback.application.config.ProfileImageConfig;

@SpringBootApplication(scanBasePackages = {
	"com.pinback.api",
	"com.pinback.application",
	"com.pinback.infrastructure",
	"com.pinback.shared"
})
@EntityScan("com.pinback.domain")
@EnableJpaRepositories("com.pinback.infrastructure")
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties({ProfileImageConfig.class, JobImageConfig.class})
public class PinbackApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(PinbackApiApplication.class, args);
	}
}
