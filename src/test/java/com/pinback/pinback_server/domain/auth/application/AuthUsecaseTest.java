package com.pinback.pinback_server.domain.auth.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.auth.application.command.SignUpCommand;
import com.pinback.pinback_server.domain.auth.presentation.dto.request.KeyCommand;
import com.pinback.pinback_server.domain.auth.presentation.dto.request.NotificationInfoCommand;
import com.pinback.pinback_server.domain.auth.presentation.dto.response.SignUpResponse;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.domain.user.domain.repository.UserRepository;
import com.pinback.pinback_server.global.common.jwt.JwtUtil;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthUsecaseTest {

	@Autowired
	private AuthUsecase authUsecase;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@DisplayName("사용자는 회원가입을 할 수 있다.")
	@Test
	void signupTest() {
		//given
		SignUpCommand command = SignUpCommand.of("testEmail", LocalTime.of(10, 0, 0), NotificationInfoCommand.from(
			"testEndPoint",
			new KeyCommand(
				"p256dh",
				"auth"
			)
		));
		//when
		SignUpResponse response = authUsecase.signUp(command);

		//then
		List<User> users = userRepository.findAll();
		assertThat(users).hasSize(1);
		assertThat(users.get(0).getEmail()).isEqualTo(command.email());
		assertThat(users.get(0).getRemindDefault()).isEqualTo(command.remindDefault());

	}

	@DisplayName("사용자는 회원가입 시 정상적인 토큰을 발급받는다")
	@Test
	void getValidToken() {
		//given
		SignUpCommand command = SignUpCommand.of("testEmail", LocalTime.of(10, 0, 0), NotificationInfoCommand.from(
			"testEndPoint",
			new KeyCommand(
				"p256dh",
				"auth"
			)
		));
		//when
		SignUpResponse response = authUsecase.signUp(command);

		//then
		List<User> users = userRepository.findAll();
		assertThat(jwtUtil.extractId(response.token())).isEqualTo(users.get(0).getId());
	}

}
