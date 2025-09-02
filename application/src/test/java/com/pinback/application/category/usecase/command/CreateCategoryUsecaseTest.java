package com.pinback.application.category.usecase.command;

import static com.pinback.application.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import com.pinback.application.ApplicationTestBase;
import com.pinback.application.category.dto.command.CreateCategoryCommand;
import com.pinback.application.category.dto.response.CreateCategoryResponse;
import com.pinback.application.category.port.out.CategoryColorServicePort;
import com.pinback.application.category.port.out.CategoryGetServicePort;
import com.pinback.application.category.port.out.CategorySaveServicePort;
import com.pinback.application.common.exception.CategoryAlreadyExistException;
import com.pinback.application.common.exception.CategoryLimitOverException;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.category.enums.CategoryColor;
import com.pinback.domain.user.entity.User;

class CreateCategoryUsecaseTest extends ApplicationTestBase {

	@Mock
	private CategorySaveServicePort categorySaveService;

	@Mock
	private CategoryGetServicePort categoryGetService;

	@Mock
	private CategoryColorServicePort categoryColorService;

	@InjectMocks
	private CreateCategoryUsecase createCategoryUsecase;

	private User user;
	private Category savedCategory;

	@BeforeEach
	void setUp() {
		user = user();
		ReflectionTestUtils.setField(user, "id", java.util.UUID.randomUUID());
		savedCategory = categoryWithName(user, "테스트카테고리");
		ReflectionTestUtils.setField(savedCategory, "id", 1L);
	}

	@DisplayName("사용자는 카테고리를 생성할 수 있다")
	@Test
	void createCategorySuccess() {
		// given
		CreateCategoryCommand command = new CreateCategoryCommand("새로운카테고리");

		when(categoryGetService.checkExistsByCategoryNameAndUser("새로운카테고리", user)).thenReturn(false);
		when(categoryColorService.getUsedColorsByUser(user)).thenReturn(
			Set.of(CategoryColor.COLOR1, CategoryColor.COLOR2));
		when(categorySaveService.save(any(Category.class))).thenReturn(savedCategory);

		// when
		CreateCategoryResponse response = createCategoryUsecase.createCategory(user, command);

		// then
		assertThat(response.categoryId()).isEqualTo(1L);
		assertThat(response.categoryName()).isEqualTo("테스트카테고리");
		assertThat(response.categoryColor()).isEqualTo("COLOR1");
		verify(categoryGetService).checkExistsByCategoryNameAndUser("새로운카테고리", user);
		verify(categoryColorService).getUsedColorsByUser(user);
		verify(categorySaveService).save(argThat(category ->
			category.getName().equals("새로운카테고리") &&
				category.getUser().equals(user) &&
				category.getColor().equals(CategoryColor.COLOR3)
		));
	}

	@DisplayName("카테고리가 10개 이상이면 생성할 수 없다")
	@Test
	void createCategoryExceedsLimitThrowsException() {
		// given
		CreateCategoryCommand command = new CreateCategoryCommand("새로운카테고리");

		when(categoryGetService.countCategoriesByUser(user)).thenReturn(10L);

		// when & then
		assertThatThrownBy(() -> createCategoryUsecase.createCategory(user, command))
			.isInstanceOf(CategoryLimitOverException.class);

		verify(categoryGetService).countCategoriesByUser(user);
		verifyNoMoreInteractions(categoryGetService, categoryColorService, categorySaveService);
	}

	@DisplayName("동일한 이름의 카테고리가 이미 존재하면 생성할 수 없다")
	@Test
	void createCategoryDuplicateNameThrowsException() {
		// given
		CreateCategoryCommand command = new CreateCategoryCommand("중복카테고리");

		when(categoryGetService.countCategoriesByUser(user)).thenReturn(5L);
		when(categoryGetService.checkExistsByCategoryNameAndUser("중복카테고리", user)).thenReturn(true);

		// when & then
		assertThatThrownBy(() -> createCategoryUsecase.createCategory(user, command))
			.isInstanceOf(CategoryAlreadyExistException.class);

		verify(categoryGetService).countCategoriesByUser(user);
		verify(categoryGetService).checkExistsByCategoryNameAndUser("중복카테고리", user);
		verifyNoMoreInteractions(categoryGetService, categoryColorService, categorySaveService);
	}

	@DisplayName("사용 중이지 않은 첫 번째 색상을 자동으로 할당한다")
	@Test
	void createCategoryAutoAssignFirstAvailableColor() {
		// given
		CreateCategoryCommand command = new CreateCategoryCommand("새로운카테고리");

		when(categoryGetService.countCategoriesByUser(user)).thenReturn(3L);
		when(categoryGetService.checkExistsByCategoryNameAndUser("새로운카테고리", user)).thenReturn(false);
		when(categoryColorService.getUsedColorsByUser(user)).thenReturn(
			Set.of(CategoryColor.COLOR1, CategoryColor.COLOR3));
		when(categorySaveService.save(any(Category.class))).thenReturn(savedCategory);

		// when
		createCategoryUsecase.createCategory(user, command);

		// then
		verify(categorySaveService).save(argThat(category ->
			category.getColor().equals(CategoryColor.COLOR2)
		));
	}
}
