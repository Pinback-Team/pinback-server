package com.pinback.infrastructure.user.service;

import static com.pinback.infrastructure.fixture.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.ServiceTest;
import com.pinback.infrastructure.user.repository.UserRepository;

@Import(UserSaveServiceImpl.class)
@Transactional
class UserSaveServiceTest extends ServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserSaveServiceImpl userSaveService;

	@DisplayName("사용자를 저장하면 ID가 생성되어 반환된다.")
	@Test
	void saveUserTest() {
		//given
		User user = user();

		//when
		User savedUser = userSaveService.save(user);

		//then
		assertThat(savedUser.getId()).isNotNull();
		assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
		assertThat(savedUser.getRemindDefault()).isEqualTo(user.getRemindDefault());
	}

	@DisplayName("저장된 사용자는 데이터베이스에서 조회할 수 있다.")
	@Test
	void savedUserCanBeRetrieved() {
		//given
		User user = user();

		//when
		User savedUser = userSaveService.save(user);

		//then
		User foundUser = userRepository.findById(savedUser.getId()).orElse(null);
		assertThat(foundUser).isNotNull();
		assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
		assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
	}
}
