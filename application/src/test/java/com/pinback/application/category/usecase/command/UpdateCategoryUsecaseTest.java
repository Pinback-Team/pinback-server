package com.pinback.application.category.usecase.command;

import static com.pinback.application.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import com.pinback.application.ApplicationTestBase;
import com.pinback.application.category.dto.command.UpdateCategoryCommand;
import com.pinback.application.category.dto.response.UpdateCategoryResponse;
import com.pinback.application.category.port.out.CategoryGetServicePort;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;

class UpdateCategoryUsecaseTest extends ApplicationTestBase {

	@Mock
	private CategoryGetServicePort categoryGetService;

	@InjectMocks
	private UpdateCategoryUsecase updateCategoryUsecase;

	private User user;
	private Category category;

	@BeforeEach
	void setUp() {
		user = user();
		ReflectionTestUtils.setField(user, "id", java.util.UUID.randomUUID());
		category = categoryWithName(user, "원본카테고리");
		ReflectionTestUtils.setField(category, "id", 1L);
	}

	@DisplayName("사용자는 카테고리 이름을 수정할 수 있다")
	@Test
	void updateCategory_Success() {
		// given
		Long categoryId = 1L;
		UpdateCategoryCommand command = new UpdateCategoryCommand("수정된카테고리");

		when(categoryGetService.getCategoryAndUser(categoryId, user)).thenReturn(category);

		// when
		UpdateCategoryResponse response = updateCategoryUsecase.updateCategory(user, categoryId, command);

		// then
		assertThat(response.categoryId()).isEqualTo(1L);
		assertThat(response.categoryName()).isEqualTo("수정된카테고리");
		assertThat(category.getName()).isEqualTo("수정된카테고리");
		verify(categoryGetService).getCategoryAndUser(categoryId, user);
	}

	@DisplayName("같은 이름으로 수정하려고 하면 성공한다")
	@Test
	void updateCategory_SameName_Success() {
		// given
		Long categoryId = 1L;
		String originalName = "원본카테고리";
		UpdateCategoryCommand command = new UpdateCategoryCommand(originalName);

		when(categoryGetService.getCategoryAndUser(categoryId, user)).thenReturn(category);

		// when
		UpdateCategoryResponse response = updateCategoryUsecase.updateCategory(user, categoryId, command);

		// then
		assertThat(response.categoryId()).isEqualTo(1L);
		assertThat(response.categoryName()).isEqualTo(originalName);
		verify(categoryGetService).getCategoryAndUser(categoryId, user);
	}
}
