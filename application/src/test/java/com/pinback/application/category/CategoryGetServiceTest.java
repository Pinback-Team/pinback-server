package com.pinback.application.category;

import static com.pinback.application.fixture.TestFixture.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.ServiceTest;
import com.pinback.application.category.service.CategoryGetService;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.category.exception.CategoryNotFoundException;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.category.repository.CategoryRepository;
import com.pinback.infrastructure.user.repository.UserRepository;

@Import({CategoryGetService.class})
@Transactional
public class CategoryGetServiceTest extends ServiceTest {

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
		Assertions.assertThatThrownBy(() -> categoryGetService.findByIdAndUserId(category.getId(), user1))
			.isInstanceOf(CategoryNotFoundException.class);

	}
}
