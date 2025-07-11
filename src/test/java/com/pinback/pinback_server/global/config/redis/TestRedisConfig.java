package com.pinback.pinback_server.global.config.redis;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = {"com.pinback.pinback_server"})
@PropertySource("classpath:application-test.properties")
@Import({RedisConfig.class})
@Configuration
public class TestRedisConfig {
}
