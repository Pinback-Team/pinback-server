package com.pinback.infrastructure.user.service;

import static com.pinback.infrastructure.fixture.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.common.exception.UserNotFoundException;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.ServiceTest;
import com.pinback.infrastructure.user.repository.UserRepository;

@Import(UserGetService.class)
@Transactional
class UserGetServiceTest extends ServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserGetService userGetService;

	@DisplayName("이메일로 사용자를 조회할 수 있다.")
	@Test
	void findByEmailTest() {
		//given
		User user = userRepository.save(user());
		String email = user.getEmail();

		//when
		User foundUser = userGetService.findByEmail(email);

		//then
		assertThat(foundUser.getEmail()).isEqualTo(email);
		assertThat(foundUser.getId()).isEqualTo(user.getId());
	}

	@DisplayName("존재하지 않는 이메일로 조회하면 빈 Optional을 반환한다.")
	@Test
	void findByEmailNotFoundTest() {
		//given
		String nonExistentEmail = "nonexistent@test.com";

		//when & then
		assertThatThrownBy(() -> userGetService.findByEmail(nonExistentEmail))
			.isInstanceOf(UserNotFoundException.class);
	}

	@DisplayName("ID로 사용자를 조회할 수 있다.")
	@Test
	void findByIdTest() {
		//given
		User user = userRepository.save(user());

		//when
		User foundUser = userGetService.findById(user.getId());

		//then
		assertThat(foundUser.getId()).isEqualTo(user.getId());
		assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
	}

	@DisplayName("존재하지 않는 ID로 조회하면 예외가 발생한다.")
	@Test
	void findByIdNotFoundTest() {
		//given
		java.util.UUID nonExistentId = java.util.UUID.randomUUID();

		//when & then
		assertThatThrownBy(() -> userGetService.findById(nonExistentId))
			.isInstanceOf(UserNotFoundException.class);
	}
}
