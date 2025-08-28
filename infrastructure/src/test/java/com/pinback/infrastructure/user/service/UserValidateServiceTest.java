package com.pinback.infrastructure.user.service;

import static com.pinback.infrastructure.fixture.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.common.exception.UserDuplicateException;
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
		assertThatNoException().isThrownBy(() -> userValidateService.validateDuplicateEmail(newEmail));
	}

	@DisplayName("중복된 이메일로 검증하면 예외가 발생한다.")
	@Test
	void validateDuplicateFailTest() {
		//given
		User user = userRepository.save(user());

		//when & then
		assertThatThrownBy(() -> userValidateService.validateDuplicateEmail(user.getEmail()))
			.isInstanceOf(UserDuplicateException.class);
	}
}
