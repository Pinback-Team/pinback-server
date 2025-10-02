package com.pinback.application.article.usecase.query;

import static com.pinback.application.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import com.pinback.application.ApplicationTestBase;
import com.pinback.application.article.dto.ArticlesWithUnreadCountDto;
import com.pinback.application.article.dto.RemindArticlesWithCountDto;
import com.pinback.application.article.dto.query.PageQuery;
import com.pinback.application.article.dto.response.ArticleDetailResponse;
import com.pinback.application.article.dto.response.ArticlesPageResponse;
import com.pinback.application.article.dto.response.GetAllArticlesResponse;
import com.pinback.application.article.dto.response.TodayRemindResponse;
import com.pinback.application.article.port.out.ArticleGetServicePort;
import com.pinback.application.category.port.in.GetCategoryPort;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;

class GetArticleUsecaseTest extends ApplicationTestBase {

	@Mock
	private ArticleGetServicePort articleGetServicePort;
	@Mock
	private GetCategoryPort getCategoryPort;

	@InjectMocks
	private GetArticleUsecase getArticleUsecase;

	private User user;
	private Category category;
	private Article article;

	@BeforeEach
	void setUp() {
		user = user();
		ReflectionTestUtils.setField(user, "id", java.util.UUID.randomUUID());
		ReflectionTestUtils.setField(user, "createdAt", LocalDateTime.now().minusDays(5));
		category = categoryWithName(user, "테스트 카테고리");
		ReflectionTestUtils.setField(category, "id", 1L);
		article = articleWithDate(user, "https://test.com", category, LocalDateTime.of(2025, 8, 20, 15, 0));
		ReflectionTestUtils.setField(article, "id", 1L);
	}

	@DisplayName("아티클 상세 정보를 조회할 수 있다")
	@Test
	void getArticleDetail_Success() {
		// given
		Long articleId = 1L;
		when(articleGetServicePort.findById(articleId)).thenReturn(article);

		// when
		ArticleDetailResponse response = getArticleUsecase.getArticleDetail(articleId);

		// then
		assertThat(response).isNotNull();
		assertThat(response.url()).isEqualTo(article.getUrl());
		assertThat(response.memo()).isEqualTo(article.getMemo());
		assertThat(response.remindAt()).isEqualTo(article.getRemindAt());
		verify(articleGetServicePort).findById(articleId);
	}

	@DisplayName("URL로 아티클 존재 여부를 확인할 수 있다")
	@Test
	void checkArticleExists_Found() {
		// given
		String url = "https://test.com";
		when(articleGetServicePort.findByUrlAndUser(user, url)).thenReturn(Optional.of(article));

		// when
		ArticleDetailResponse response = getArticleUsecase.checkArticleExists(user, url);

		// then
		assertThat(response).isNotNull();
		assertThat(response.url()).isEqualTo(article.getUrl());
		verify(articleGetServicePort).findByUrlAndUser(user, url);
	}

	@DisplayName("존재하지 않는 URL로 조회하면 null을 반환한다")
	@Test
	void checkArticleExists_NotFound() {
		// given
		String url = "https://nonexistent.com";
		when(articleGetServicePort.findByUrlAndUser(user, url)).thenReturn(Optional.empty());

		// when
		ArticleDetailResponse response = getArticleUsecase.checkArticleExists(user, url);

		// then
		assertThat(response).isNull();
		verify(articleGetServicePort).findByUrlAndUser(user, url);
	}

	@DisplayName("사용자의 모든 아티클을 페이징으로 조회할 수 있다")
	@Test
	void getAllArticles_Success() {
		// given
		PageQuery pageQuery = new PageQuery(0, 10);
		PageRequest pageRequest = PageRequest.of(0, 10);

		List<Article> articles = List.of(article, article, article, article, article);
		Page<Article> articlePage = new PageImpl<>(articles, pageRequest, 5);
		ArticlesWithUnreadCountDto dto = new ArticlesWithUnreadCountDto(3L, articlePage, null);

		when(articleGetServicePort.findAll(user, pageRequest)).thenReturn(dto);

		// when
		GetAllArticlesResponse response = getArticleUsecase.getAllArticles(user, pageQuery);

		// then
		assertThat(response.articles()).hasSize(5);
		assertThat(response.isNewUser()).isNotNull();
		verify(articleGetServicePort).findAll(user, pageRequest);
	}

	@DisplayName("카테고리별로 아티클을 조회할 수 있다")
	@Test
	void getAllArticlesByCategory_Success() {
		// given
		Long categoryId = 1L;
		PageQuery pageQuery = new PageQuery(0, 10);
		PageRequest pageRequest = PageRequest.of(0, 10);

		List<Article> articles = List.of(article, article, article);
		Page<Article> articlePage = new PageImpl<>(articles, pageRequest, 3);
		ArticlesWithUnreadCountDto dto = new ArticlesWithUnreadCountDto(3L, articlePage, 5L);

		when(getCategoryPort.getCategoryAndUser(categoryId, user)).thenReturn(category);
		when(articleGetServicePort.findAllByCategory(user, category, true, pageRequest)).thenReturn(dto);

		// when
		ArticlesPageResponse response = getArticleUsecase.getAllArticlesByCategory(user, categoryId, true, pageQuery);

		// then
		assertThat(response.articles()).hasSize(3);
		verify(getCategoryPort).getCategoryAndUser(categoryId, user);
		verify(articleGetServicePort).findAllByCategory(user, category, true, pageRequest);
	}

	@DisplayName("읽지 않은 아티클만 조회할 수 있다")
	@Test
	void getUnreadArticles_Success() {
		// given
		PageQuery pageQuery = new PageQuery(0, 10);
		PageRequest pageRequest = PageRequest.of(0, 10);

		List<Article> articles = List.of(article, article, article);
		Page<Article> articlePage = new PageImpl<>(articles, pageRequest, 3);
		ArticlesWithUnreadCountDto dto = new ArticlesWithUnreadCountDto(3L, articlePage, null);

		when(articleGetServicePort.findUnreadArticles(user, pageRequest)).thenReturn(dto);

		// when
		ArticlesPageResponse response = getArticleUsecase.getUnreadArticles(user, pageQuery);

		// then
		assertThat(response.articles()).hasSize(3);
		verify(articleGetServicePort).findUnreadArticles(user, pageRequest);
	}

	@DisplayName("오늘 리마인드할 읽은 아티클을 조회할 수 있다")
	@Test
	void getRemindArticles_ReadStatus_Success() {
		// given
		User userWithRemindTime = User.create("test@example.com", LocalTime.of(14, 0));
		ReflectionTestUtils.setField(userWithRemindTime, "id", java.util.UUID.randomUUID());

		LocalDateTime now = LocalDateTime.of(2025, 8, 20, 15, 0);
		LocalDateTime startOfDay = LocalDateTime.of(2025, 8, 20, 0, 0);
		LocalDateTime endOfDay = LocalDateTime.of(2025, 8, 20, 23, 59, 59, 999999999);
		PageQuery pageQuery = new PageQuery(0, 10);
		PageRequest pageRequest = PageRequest.of(0, 10);

		List<Article> articles = List.of(article, article);
		Page<Article> articlePage = new PageImpl<>(articles, pageRequest, 2);
		RemindArticlesWithCountDto dto = new RemindArticlesWithCountDto(2L, 3L, articlePage);

		when(articleGetServicePort.findTodayRemindWithCount(userWithRemindTime, startOfDay, endOfDay, pageRequest, true)).thenReturn(dto);

		// when
		TodayRemindResponse response = getArticleUsecase.getRemindArticles(userWithRemindTime, now, true, pageQuery);

		// then
		assertThat(response.articles()).hasSize(2);
		assertThat(response.readArticleCount()).isEqualTo(2L);
		assertThat(response.unreadArticleCount()).isEqualTo(3L);
		verify(articleGetServicePort).findTodayRemindWithCount(userWithRemindTime, startOfDay, endOfDay, pageRequest, true);
	}

	@DisplayName("오늘 리마인드할 안읽은 아티클을 조회할 수 있다")
	@Test
	void getRemindArticles_UnreadStatus_Success() {
		// given
		User userWithRemindTime = User.create("test@example.com", LocalTime.of(14, 0));
		ReflectionTestUtils.setField(userWithRemindTime, "id", java.util.UUID.randomUUID());

		LocalDateTime now = LocalDateTime.of(2025, 8, 20, 15, 0);
		LocalDateTime startOfDay = LocalDateTime.of(2025, 8, 20, 0, 0);
		LocalDateTime endOfDay = LocalDateTime.of(2025, 8, 20, 23, 59, 59, 999999999);
		PageQuery pageQuery = new PageQuery(0, 10);
		PageRequest pageRequest = PageRequest.of(0, 10);

		List<Article> articles = List.of(article, article, article);
		Page<Article> articlePage = new PageImpl<>(articles, pageRequest, 3);
		RemindArticlesWithCountDto dto = new RemindArticlesWithCountDto(2L, 3L, articlePage);

		when(articleGetServicePort.findTodayRemindWithCount(userWithRemindTime, startOfDay, endOfDay, pageRequest, false)).thenReturn(dto);

		// when
		TodayRemindResponse response = getArticleUsecase.getRemindArticles(userWithRemindTime, now, false, pageQuery);

		// then
		assertThat(response.articles()).hasSize(3);
		assertThat(response.readArticleCount()).isEqualTo(2L);
		assertThat(response.unreadArticleCount()).isEqualTo(3L);
		verify(articleGetServicePort).findTodayRemindWithCount(userWithRemindTime, startOfDay, endOfDay, pageRequest, false);
	}
}
