package com.pinback.pinback_server.global.common.jwt;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class JwtProviderTest {
	@Autowired
	private JwtProvider jwtProvider;

	@DisplayName("jwt 토큰을 생성할 수 있다.")
	@Test
	void jwtCreateTest() {
		//given
		UUID id = UUID.randomUUID();

		//when
		String token = jwtProvider.createAccessToken(id);

		//then
		assertThat(token).isNotBlank();
	}
}
