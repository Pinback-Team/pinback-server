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
import com.pinback.application.article.port.out.ArticleDeleteServicePort;
import com.pinback.application.category.port.out.CategoryDeleteServicePort;
import com.pinback.application.category.port.out.CategoryGetServicePort;
import com.pinback.application.common.exception.CategoryNotOwnedException;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;

class DeleteCategoryUsecaseTest extends ApplicationTestBase {

	@Mock
	private CategoryGetServicePort categoryGetService;

	@Mock
	private CategoryDeleteServicePort categoryDeleteService;

	@Mock
	private ArticleDeleteServicePort articleDeleteService;

	@InjectMocks
	private DeleteCategoryUsecase deleteCategoryUsecase;

	private User user;
	private User otherUser;
	private Category category;

	@BeforeEach
	void setUp() {
		user = user();
		ReflectionTestUtils.setField(user, "id", java.util.UUID.randomUUID());

		otherUser = userWithEmail("other@test.com");
		ReflectionTestUtils.setField(otherUser, "id", java.util.UUID.randomUUID());

		category = categoryWithName(user, "테스트카테고리");
		ReflectionTestUtils.setField(category, "id", 1L);
	}

	@DisplayName("사용자는 자신의 카테고리를 삭제할 수 있다")
	@Test
	void deleteCategory_Success() {
		// given
		Long categoryId = 1L;

		when(categoryGetService.findById(categoryId)).thenReturn(category);

		// when
		deleteCategoryUsecase.deleteCategory(user, categoryId);

		// then
		verify(categoryGetService).findById(categoryId);
		verify(articleDeleteService).deleteByCategory(user, categoryId);
		verify(categoryDeleteService).delete(category);
	}

	@DisplayName("다른 사용자의 카테고리를 삭제하려고 하면 예외가 발생한다")
	@Test
	void deleteCategory_NotOwner_ThrowsException() {
		// given
		Long categoryId = 1L;

		when(categoryGetService.findById(categoryId)).thenReturn(category);

		// when & then
		assertThatThrownBy(() -> deleteCategoryUsecase.deleteCategory(otherUser, categoryId))
			.isInstanceOf(CategoryNotOwnedException.class);

		verify(categoryGetService).findById(categoryId);
		verifyNoInteractions(articleDeleteService, categoryDeleteService);
	}

	@DisplayName("카테고리 삭제 시 해당 카테고리의 모든 아티클도 함께 삭제된다")
	@Test
	void deleteCategory_DeletesAssociatedArticles() {
		// given
		Long categoryId = 1L;

		when(categoryGetService.findById(categoryId)).thenReturn(category);

		// when
		deleteCategoryUsecase.deleteCategory(user, categoryId);

		// then
		verify(articleDeleteService).deleteByCategory(user, categoryId);
		verify(categoryDeleteService).delete(category);

		// 아티클 삭제가 카테고리 삭제보다 먼저 호출되는지 확인
		var inOrder = inOrder(articleDeleteService, categoryDeleteService);
		inOrder.verify(articleDeleteService).deleteByCategory(user, categoryId);
		inOrder.verify(categoryDeleteService).delete(category);
	}

	@DisplayName("존재하지 않는 카테고리 ID로 삭제를 시도하면 예외가 전파된다")
	@Test
	void deleteCategory_CategoryNotFound_ThrowsException() {
		// given
		Long nonExistentCategoryId = 999L;

		when(categoryGetService.findById(nonExistentCategoryId))
			.thenThrow(new RuntimeException("Category not found"));

		// when & then
		assertThatThrownBy(() -> deleteCategoryUsecase.deleteCategory(user, nonExistentCategoryId))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("Category not found");

		verify(categoryGetService).findById(nonExistentCategoryId);
		verifyNoInteractions(articleDeleteService, categoryDeleteService);
	}

	@DisplayName("카테고리 소유자 검증이 올바르게 작동한다")
	@Test
	void deleteCategory_OwnershipValidation() {
		// given
		Category otherUserCategory = categoryWithName(otherUser, "다른사용자카테고리");
		ReflectionTestUtils.setField(otherUserCategory, "id", 2L);
		Long categoryId = 2L;

		when(categoryGetService.findById(categoryId)).thenReturn(otherUserCategory);

		// when & then
		assertThatThrownBy(() -> deleteCategoryUsecase.deleteCategory(user, categoryId))
			.isInstanceOf(CategoryNotOwnedException.class);

		verify(categoryGetService).findById(categoryId);
		verifyNoInteractions(articleDeleteService, categoryDeleteService);
	}
}
