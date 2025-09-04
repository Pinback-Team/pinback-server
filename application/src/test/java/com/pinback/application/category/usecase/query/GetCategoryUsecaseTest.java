package com.pinback.application.category.usecase.query;

import static com.pinback.application.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import com.pinback.application.ApplicationTestBase;
import com.pinback.application.article.port.out.ArticleGetServicePort;
import com.pinback.application.category.dto.CategoriesForDashboardDto;
import com.pinback.application.category.dto.CategoryForDashboardDto;
import com.pinback.application.category.dto.response.CategoriesForDashboardResponse;
import com.pinback.application.category.dto.response.CategoriesForExtensionResponse;
import com.pinback.application.category.port.out.CategoryGetServicePort;
import com.pinback.application.common.exception.CategoryNotFoundException;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;

class GetCategoryUsecaseTest extends ApplicationTestBase {

	@Mock
	private CategoryGetServicePort categoryGetServicePort;

	@Mock
	private ArticleGetServicePort articleGetServicePort;

	@InjectMocks
	private GetCategoryUsecase getCategoryUsecase;

	private User user;
	private Category category1;
	private Category category2;
	private Article recentArticle;

	@BeforeEach
	void setUp() {
		user = user();
		ReflectionTestUtils.setField(user, "id", java.util.UUID.randomUUID());

		category1 = categoryWithName(user, "카테고리1");
		ReflectionTestUtils.setField(category1, "id", 1L);

		category2 = categoryWithName(user, "카테고리2");
		ReflectionTestUtils.setField(category2, "id", 2L);

		recentArticle = articleWithCategory(user, category1);
		ReflectionTestUtils.setField(recentArticle, "id", 1L);
	}

	@DisplayName("익스텐션용 카테고리 목록을 조회할 수 있다")
	@Test
	void getAllCategoriesForExtension_Success() {
		// given
		List<Category> categories = List.of(category1, category2);

		when(articleGetServicePort.findRecentByUser(user)).thenReturn(Optional.of(recentArticle));
		when(categoryGetServicePort.findAllForExtension(user.getId())).thenReturn(categories);

		// when
		CategoriesForExtensionResponse response = getCategoryUsecase.getAllCategoriesForExtension(user);

		// then
		assertThat(response.recentSaved()).isEqualTo("카테고리1");
		assertThat(response.categories()).hasSize(2);
		assertThat(response.categories().get(0).categoryName()).isEqualTo("카테고리1");
		assertThat(response.categories().get(1).categoryName()).isEqualTo("카테고리2");

		verify(articleGetServicePort).findRecentByUser(user);
		verify(categoryGetServicePort).findAllForExtension(user.getId());
	}

	@DisplayName("최근 저장된 아티클이 없으면 recentSaved가 null이다")
	@Test
	void getAllCategoriesForExtension_NoRecentArticle() {
		// given
		List<Category> categories = List.of(category1, category2);

		when(articleGetServicePort.findRecentByUser(user)).thenReturn(Optional.empty());
		when(categoryGetServicePort.findAllForExtension(user.getId())).thenReturn(categories);

		// when
		CategoriesForExtensionResponse response = getCategoryUsecase.getAllCategoriesForExtension(user);

		// then
		assertThat(response.recentSaved()).isNull();
		assertThat(response.categories()).hasSize(2);

		verify(articleGetServicePort).findRecentByUser(user);
		verify(categoryGetServicePort).findAllForExtension(user.getId());
	}

	@DisplayName("대시보드용 카테고리 목록을 조회할 수 있다")
	@Test
	void getAllCategoriesForDashboard_Success() {
		// given
		List<CategoryForDashboardDto> categories = List.of(
			new CategoryForDashboardDto(1L, "카테고리1", 3L),
			new CategoryForDashboardDto(2L, "카테고리2", 1L)
		);
		CategoriesForDashboardDto dashboardDto = new CategoriesForDashboardDto(categories);

		when(categoryGetServicePort.findAllForDashboard(user.getId())).thenReturn(dashboardDto);

		// when
		CategoriesForDashboardResponse response = getCategoryUsecase.getAllCategoriesForDashboard(user);

		// then
		assertThat(response.categories()).hasSize(2);
		assertThat(response.categories().get(0).id()).isEqualTo(1L);
		assertThat(response.categories().get(0).name()).isEqualTo("카테고리1");
		assertThat(response.categories().get(0).unreadCount()).isEqualTo(3L);
		assertThat(response.categories().get(1).id()).isEqualTo(2L);
		assertThat(response.categories().get(1).name()).isEqualTo("카테고리2");
		assertThat(response.categories().get(1).unreadCount()).isEqualTo(1L);

		verify(categoryGetServicePort).findAllForDashboard(user.getId());
	}

	@DisplayName("빈 카테고리 목록도 올바르게 처리한다")
	@Test
	void getAllCategoriesForDashboard_EmptyList() {
		// given
		CategoriesForDashboardDto dashboardDto = new CategoriesForDashboardDto(List.of());

		when(categoryGetServicePort.findAllForDashboard(user.getId())).thenReturn(dashboardDto);

		// when
		CategoriesForDashboardResponse response = getCategoryUsecase.getAllCategoriesForDashboard(user);

		// then
		assertThat(response.categories()).isEmpty();
		verify(categoryGetServicePort).findAllForDashboard(user.getId());
	}

	@DisplayName("특정 카테고리를 소유자와 함께 조회할 수 있다")
	@Test
	void getCategoryAndUser_Success() {
		// given
		Long categoryId = 1L;

		when(categoryGetServicePort.getCategoryAndUser(categoryId, user)).thenReturn(category1);

		// when
		Category result = getCategoryUsecase.getCategoryAndUser(categoryId, user);

		// then
		assertThat(result).isEqualTo(category1);
		assertThat(result.getName()).isEqualTo("카테고리1");
		assertThat(result.getUser()).isEqualTo(user);

		verify(categoryGetServicePort).getCategoryAndUser(categoryId, user);
	}

	@DisplayName("존재하지 않는 카테고리를 조회하면 예외가 전파된다")
	@Test
	void getCategoryAndUser_CategoryNotFound() {
		// given
		Long nonExistentCategoryId = 999L;

		when(categoryGetServicePort.getCategoryAndUser(nonExistentCategoryId, user))
			.thenThrow(new CategoryNotFoundException());

		// when & then
		assertThatThrownBy(() -> getCategoryUsecase.getCategoryAndUser(nonExistentCategoryId, user))
			.isInstanceOf(CategoryNotFoundException.class);

		verify(categoryGetServicePort).getCategoryAndUser(nonExistentCategoryId, user);
	}
}