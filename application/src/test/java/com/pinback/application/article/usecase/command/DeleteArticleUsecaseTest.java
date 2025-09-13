package com.pinback.application.article.usecase.command;

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
import com.pinback.application.article.port.out.ArticleGetServicePort;
import com.pinback.application.common.exception.ArticleNotOwnedException;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;

class DeleteArticleUsecaseTest extends ApplicationTestBase {

	@Mock
	private ArticleGetServicePort articleGetService;
	@Mock
	private ArticleDeleteServicePort articleDeleteService;

	@InjectMocks
	private DeleteArticleUsecase deleteArticleUsecase;

	private User user;
	private User otherUser;
	private Category category;
	private Article article;

	@BeforeEach
	void setUp() {
		user = user();
		ReflectionTestUtils.setField(user, "id", java.util.UUID.randomUUID());

		otherUser = userWithEmail("other@example.com");
		ReflectionTestUtils.setField(otherUser, "id", java.util.UUID.randomUUID());

		category = category(user);
		article = articleWithCategory(user, category);
	}

	@DisplayName("사용자는 자신의 아티클을 삭제할 수 있다")
	@Test
	void deleteArticle_Success() {
		// given
		Long articleId = 1L;
		when(articleGetService.findById(articleId)).thenReturn(article);

		// when
		deleteArticleUsecase.deleteArticle(user, articleId);

		// then
		verify(articleGetService).findById(articleId);
		verify(articleDeleteService).delete(article);
	}

	@DisplayName("다른 사용자의 아티클은 삭제할 수 없다")
	@Test
	void deleteArticle_NotOwned_ThrowsException() {
		// given
		Long articleId = 1L;
		when(articleGetService.findById(articleId)).thenReturn(article);

		// when & then
		assertThatThrownBy(() -> deleteArticleUsecase.deleteArticle(otherUser, articleId))
			.isInstanceOf(ArticleNotOwnedException.class);

		verify(articleGetService).findById(articleId);
		verifyNoInteractions(articleDeleteService);
	}

	@DisplayName("존재하지 않는 아티클 삭제 시 예외가 발생한다")
	@Test
	void deleteArticle_NotFound_ThrowsException() {
		// given
		Long nonExistentArticleId = 999L;
		when(articleGetService.findById(nonExistentArticleId))
			.thenThrow(new RuntimeException("Article not found"));

		// when & then
		assertThatThrownBy(() -> deleteArticleUsecase.deleteArticle(user, nonExistentArticleId))
			.isInstanceOf(RuntimeException.class);

		verify(articleGetService).findById(nonExistentArticleId);
		verifyNoInteractions(articleDeleteService);
	}
}
