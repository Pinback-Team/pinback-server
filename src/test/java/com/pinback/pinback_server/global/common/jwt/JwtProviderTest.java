package com.pinback.pinback_server.global.common.jwt;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JwtProvider.class)
@TestPropertySource(locations = "classpath:application-test.properties")
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
