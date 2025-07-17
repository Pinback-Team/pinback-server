package com.pinback.pinback_server.domain;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pinback.pinback_server.domain.fixture.CustomRepository;
import com.pinback.pinback_server.global.config.firebase.FirebaseConfig;

@SpringBootTest
@ActiveProfiles("test")
public class ApplicationTest {

	@MockitoBean
	protected RedisTemplate<String, Object> redisTemplate;
	@MockitoBean
	protected StringRedisTemplate stringRedisTemplate;
	@MockitoBean
	protected RedisConnectionFactory redisConnectionFactory;
	@MockitoBean
	protected RedisMessageListenerContainer redisMessageListenerContainer;
	@MockitoBean
	protected FirebaseApp firebaseApp;
	@MockitoBean
	protected FirebaseMessaging firebaseMessaging;
	@MockitoBean
	protected FirebaseConfig firebaseConfig;
	@Autowired
	CustomRepository customRepository;

	@AfterEach
	void tearDown() {
		customRepository.clearAndReset();
	}
}
