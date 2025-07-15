package com.pinback.pinback_server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class PinbackServerApplicationTests {
	@MockitoBean
	private org.springframework.data.redis.listener.RedisMessageListenerContainer RedisMessageListenerContainer;

	@Test
	void contextLoads() {
	}

}
