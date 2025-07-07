package com.pinback.pinback_server.domain.category.domain.service;

import static com.pinback.pinback_server.domain.fixture.TestFixture.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.repository.CategoryRepository;
import com.pinback.pinback_server.domain.category.exception.CategoryNotFoundException;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.domain.user.domain.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CategoryGetServiceTest {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryGetService categoryGetService;

	@DisplayName("카테고리 소유자가 아닐경우 예외가 발생한다.")
	@Test
	void throwExceptionIsNotOwner() {
		//given
		User user = userRepository.save(user());
		User user1 = userRepository.save(userWithEmail("another@gmail.com"));
		Category category = categoryRepository.save(category(user));

		//when & Then

		Assertions.assertThatThrownBy(() -> categoryGetService.getCategoryAndUser(category.getId(), user1))
			.isInstanceOf(CategoryNotFoundException.class);

	}

}
