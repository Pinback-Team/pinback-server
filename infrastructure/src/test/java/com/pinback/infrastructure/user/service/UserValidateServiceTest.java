package com.pinback.infrastructure.user.service;

import static com.pinback.infrastructure.fixture.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.common.exception.UserDuplicateException;
import com.pinback.application.common.exception.UserNotFoundException;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.ServiceTest;
import com.pinback.infrastructure.user.repository.UserRepository;

@Import(UserValidateService.class)
@Transactional
class UserValidateServiceTest extends ServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserValidateService userValidateService;

	@DisplayName("중복되지 않은 이메일은 검증을 통과한다.")
	@Test
	void validateDuplicateSuccessTest() {
		//given
		String newEmail = "new@test.com";

		//when & then
		assertThatNoException().isThrownBy(() -> userValidateService.validateDuplicate(newEmail));
	}

	@DisplayName("중복된 이메일로 검증하면 예외가 발생한다.")
	@Test
	void validateDuplicateFailTest() {
		//given
		User user = userRepository.save(user());

		//when & then
		assertThatThrownBy(() -> userValidateService.validateDuplicate(user.getEmail()))
			.isInstanceOf(UserDuplicateException.class);
	}

	@DisplayName("존재하는 이메일로 로그인 검증하면 참을 반환한다.")
	@Test
	void validateLoginSuccessTest() {
		//given
		User user = userRepository.save(user());

		//when
		boolean result = userValidateService.validateLogin(user.getEmail(), "password");

		//then
		assertThat(result).isTrue();
	}

	@DisplayName("존재하지 않는 이메일로 로그인 검증하면 거짓을 반환한다.")
	@Test
	void validateLoginFailTest() {
		//given
		String nonExistentEmail = "nonexistent@test.com";

		//when
		boolean result = userValidateService.validateLogin(nonExistentEmail, "password");

		//then
		assertThat(result).isFalse();
	}

	@DisplayName("존재하는 이메일로 인증하면 사용자를 반환한다.")
	@Test
	void authenticateSuccessTest() {
		//given
		User user = userRepository.save(user());

		//when
		User authenticatedUser = userValidateService.authenticate(user.getEmail(), "password");

		//then
		assertThat(authenticatedUser.getId()).isEqualTo(user.getId());
		assertThat(authenticatedUser.getEmail()).isEqualTo(user.getEmail());
	}

	@DisplayName("존재하지 않는 이메일로 인증하면 예외가 발생한다.")
	@Test
	void authenticateFailTest() {
		//given
		String nonExistentEmail = "nonexistent@test.com";

		//when & then
		assertThatThrownBy(() -> userValidateService.authenticate(nonExistentEmail, "password"))
			.isInstanceOf(UserNotFoundException.class);
	}
}
