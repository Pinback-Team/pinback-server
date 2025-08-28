package com.pinback.application.article.usecase.command;

import static com.pinback.application.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.pinback.application.ApplicationTestBase;
import com.pinback.application.article.dto.AcornCollectResult;
import com.pinback.application.article.dto.response.ReadArticleResponse;
import com.pinback.application.article.port.out.ArticleGetServicePort;
import com.pinback.application.common.exception.ArticleNotFoundException;
import com.pinback.application.common.exception.ArticleNotOwnedException;
import com.pinback.application.user.port.in.ManageAcornPort;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;

class UpdateArticleStatusUsecaseTest extends ApplicationTestBase {

	@Mock
	private ArticleGetServicePort articleGetService;
	@Mock
	private ManageAcornPort manageAcornPort;

	@InjectMocks
	private UpdateArticleStatusUsecase updateArticleStatusUsecase;

	private User user;
	private Category category;
	private Article unreadArticle;
	private Article readArticle;

	@BeforeEach
	void setUp() {
		user = user();
		category = category(user);
		unreadArticle = articleWithCategory(user, category);
		readArticle = readArticle(user, "https://read-test.com", category);
	}

	@DisplayName("읽지 않은 아티클을 읽음으로 변경하면 도토리를 수집할 수 있다")
	@Test
	void updateArticleStatus_UnreadToRead_CollectAcorns() {
		// given
		Long articleId = 1L;
		when(articleGetService.findByUserAndId(user, articleId)).thenReturn(unreadArticle);
		when(manageAcornPort.getCurrentAcorns(user.getId())).thenReturn(10);
		when(manageAcornPort.tryCollectAcorns(user)).thenReturn(new AcornCollectResult(15, true));

		// when
		ReadArticleResponse response = updateArticleStatusUsecase.updateArticleStatus(user, articleId);

		// then
		assertThat(response.acornCount()).isEqualTo(15);
		assertThat(response.acornCollected()).isTrue();

		verify(articleGetService).findByUserAndId(user, articleId);
		verify(manageAcornPort).getCurrentAcorns(user.getId());
		verify(manageAcornPort).tryCollectAcorns(user);
	}

	@DisplayName("이미 읽은 아티클의 상태를 변경하면 도토리를 수집하지 않는다")
	@Test
	void updateArticleStatus_AlreadyRead_NoCollectAcorns() {
		// given
		Long articleId = 2L;
		when(articleGetService.findByUserAndId(user, articleId)).thenReturn(readArticle);
		when(manageAcornPort.getCurrentAcorns(user.getId())).thenReturn(10);

		// when
		ReadArticleResponse response = updateArticleStatusUsecase.updateArticleStatus(user, articleId);

		// then
		assertThat(response.acornCount()).isEqualTo(10);
		assertThat(response.acornCollected()).isFalse();

		verify(articleGetService).findByUserAndId(user, articleId);
		verify(manageAcornPort).getCurrentAcorns(user.getId());
		verify(manageAcornPort, never()).tryCollectAcorns(any());
	}

	@DisplayName("도토리 수집에 실패해도 아티클은 읽음으로 변경된다")
	@Test
	void updateArticleStatus_AcornCollectFailed_ArticleStillMarkedAsRead() {
		// given
		Long articleId = 3L;
		when(articleGetService.findByUserAndId(user, articleId)).thenReturn(unreadArticle);
		when(manageAcornPort.getCurrentAcorns(user.getId())).thenReturn(10);
		when(manageAcornPort.tryCollectAcorns(user)).thenReturn(new AcornCollectResult(10, false));

		// when
		ReadArticleResponse response = updateArticleStatusUsecase.updateArticleStatus(user, articleId);

		// then
		assertThat(response.acornCount()).isEqualTo(10);
		assertThat(response.acornCollected()).isFalse();

		verify(articleGetService).findByUserAndId(user, articleId);
		verify(manageAcornPort).getCurrentAcorns(user.getId());
		verify(manageAcornPort).tryCollectAcorns(user);
	}

	@DisplayName("존재하지 않는 아티클의 상태 변경 시 예외가 발생한다")
	@Test
	void updateArticleStatus_ArticleNotFound_ThrowsException() {
		// given
		Long nonExistentArticleId = 999L;
		when(articleGetService.findByUserAndId(user, nonExistentArticleId))
			.thenThrow(new ArticleNotFoundException());

		// when & then
		assertThatThrownBy(() -> updateArticleStatusUsecase.updateArticleStatus(user, nonExistentArticleId))
			.isInstanceOf(ArticleNotFoundException.class);

		verify(articleGetService).findByUserAndId(user, nonExistentArticleId);
		verifyNoInteractions(manageAcornPort);
	}

	@DisplayName("다른 사용자의 아티클 상태 변경 시 예외가 발생한다")
	@Test
	void updateArticleStatus_NotOwnedArticle_ThrowsException() {
		// given
		User otherUser = userWithEmail("other@example.com");
		Long articleId = 4L;
		when(articleGetService.findByUserAndId(otherUser, articleId))
			.thenThrow(new ArticleNotOwnedException());

		// when & then
		assertThatThrownBy(() -> updateArticleStatusUsecase.updateArticleStatus(otherUser, articleId))
			.isInstanceOf(ArticleNotOwnedException.class);

		verify(articleGetService).findByUserAndId(otherUser, articleId);
		verifyNoInteractions(manageAcornPort);
	}
}
