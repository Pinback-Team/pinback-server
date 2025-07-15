package com.pinback.pinback_server.global.common.jwt;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class JwtUtilTest {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private JwtProvider jwtProvider;

	@MockitoBean
	private org.springframework.data.redis.listener.RedisMessageListenerContainer RedisMessageListenerContainer;

	@DisplayName("토큰에서 올바르게 유저 ID를 추출한다.")
	@Test
	void test() {
		//given
		UUID userId = UUID.randomUUID();
		String token = jwtProvider.createAccessToken(userId);

		//when
		UUID extractedId = jwtUtil.extractId(token);
		//then

		assertThat(extractedId).isEqualTo(userId);

	}

}