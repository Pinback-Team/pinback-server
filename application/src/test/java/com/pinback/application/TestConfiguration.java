package com.pinback.application;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.pinback.domain", "com.pinback.application", "com.pinback.infrastructure"})
@EnableJpaRepositories(
	basePackages = {"com.pinback.infrastructure"},
	repositoryImplementationPostfix = "Impl"
)
@EntityScan(basePackages = "com.pinback.domain")
public class TestConfiguration {
}
