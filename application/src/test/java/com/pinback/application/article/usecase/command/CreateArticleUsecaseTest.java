package com.pinback.application.article.usecase.command;

import static com.pinback.application.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.pinback.application.ApplicationTestBase;
import com.pinback.application.article.dto.command.ArticleCreateCommand;
import com.pinback.application.article.port.out.ArticleGetServicePort;
import com.pinback.application.article.port.out.ArticleSaveServicePort;
import com.pinback.application.category.port.in.GetCategoryPort;
import com.pinback.application.common.exception.ArticleAlreadyExistException;
import com.pinback.application.common.exception.MemoLengthLimitException;
import com.pinback.application.notification.port.in.GetPushSubscriptionPort;
import com.pinback.application.notification.port.in.ScheduleArticleReminderPort;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.notification.entity.PushSubscription;
import com.pinback.domain.user.entity.User;

class CreateArticleUsecaseTest extends ApplicationTestBase {

	@Mock
	private ArticleGetServicePort articleGetService;
	@Mock
	private ArticleSaveServicePort articleSaveService;
	@Mock
	private GetCategoryPort getCategoryPort;
	@Mock
	private GetPushSubscriptionPort getPushSubscription;
	@Mock
	private ScheduleArticleReminderPort scheduleArticleReminder;

	@InjectMocks
	private CreateArticleUsecase createArticleUsecase;

	private User user;
	private Category category;
	private Article article;
	private PushSubscription pushSubscription;

	@BeforeEach
	void setUp() {
		user = user();
		category = category(user);
		article = articleWithCategory(user, category);
		pushSubscription = pushSubscription(user);
	}

	@DisplayName("사용자는 아티클을 생성할 수 있다")
	@Test
	void createArticle_Success() {
		// given
		ArticleCreateCommand command = new ArticleCreateCommand(
			"https://test-url.com",
			1L,
			"테스트 메모",
			LocalDateTime.of(2025, 8, 20, 15, 0)
		);

		when(articleGetService.checkExistsByUserAndUrl(user, command.url())).thenReturn(false);
		when(getCategoryPort.getCategoryAndUser(command.categoryId(), user)).thenReturn(category);
		when(articleSaveService.save(any(Article.class))).thenReturn(article);

		// when
		createArticleUsecase.createArticle(user, command);

		// then
		verify(articleGetService).checkExistsByUserAndUrl(user, command.url());
		verify(getCategoryPort).getCategoryAndUser(command.categoryId(), user);
		verify(articleSaveService).save(any(Article.class));
	}

	@DisplayName("사용자는 중복된 URL로 아티클을 생성할 수 없다")
	@Test
	void createArticle_DuplicateUrl_ThrowsException() {
		// given
		ArticleCreateCommand command = new ArticleCreateCommand(
			"https://existing-url.com",
			1L,
			"새로운 메모",
			LocalDateTime.of(2025, 8, 20, 15, 0)
		);

		when(articleGetService.checkExistsByUserAndUrl(user, command.url())).thenReturn(true);

		// when & then
		assertThatThrownBy(() -> createArticleUsecase.createArticle(user, command))
			.isInstanceOf(ArticleAlreadyExistException.class);

		verify(articleGetService).checkExistsByUserAndUrl(user, command.url());
		verifyNoInteractions(getCategoryPort, articleSaveService, getPushSubscription, scheduleArticleReminder);
	}

	@DisplayName("메모가 500자를 초과하면 예외가 발생한다")
	@Test
	void createArticle_MemoTooLong_ThrowsException() {
		// given
		String longMemo = "a".repeat(501);
		ArticleCreateCommand command = new ArticleCreateCommand(
			"https://test-url.com",
			1L,
			longMemo,
			LocalDateTime.of(2025, 8, 20, 15, 0)
		);

		when(articleGetService.checkExistsByUserAndUrl(user, command.url())).thenReturn(false);

		// when & then
		assertThatThrownBy(() -> createArticleUsecase.createArticle(user, command))
			.isInstanceOf(MemoLengthLimitException.class);

		verify(articleGetService).checkExistsByUserAndUrl(user, command.url());
		verifyNoInteractions(getCategoryPort, articleSaveService, getPushSubscription, scheduleArticleReminder);
	}

	@DisplayName("과거 시간으로 리마인더를 설정하면 알림이 예약되지 않는다")
	@Test
	void createArticle_PastRemindTime_NoSchedule() {
		// given
		ArticleCreateCommand command = new ArticleCreateCommand(
			"https://test-url.com",
			1L,
			"테스트 메모",
			LocalDateTime.of(2020, 1, 1, 12, 0)
		);

		when(articleGetService.checkExistsByUserAndUrl(user, command.url())).thenReturn(false);
		when(getCategoryPort.getCategoryAndUser(command.categoryId(), user)).thenReturn(category);
		when(articleSaveService.save(any(Article.class))).thenReturn(article);

		// when
		createArticleUsecase.createArticle(user, command);

		// then
		verify(articleGetService).checkExistsByUserAndUrl(user, command.url());
		verify(getCategoryPort).getCategoryAndUser(command.categoryId(), user);
		verify(articleSaveService).save(any(Article.class));
		verifyNoInteractions(getPushSubscription, scheduleArticleReminder);
	}

	@DisplayName("리마인더 시간이 null이면 알림이 예약되지 않는다")
	@Test
	void createArticle_NullRemindTime_NoSchedule() {
		// given
		ArticleCreateCommand command = new ArticleCreateCommand(
			"https://test-url.com",
			1L,
			"테스트 메모",
			null
		);

		when(articleGetService.checkExistsByUserAndUrl(user, command.url())).thenReturn(false);
		when(getCategoryPort.getCategoryAndUser(command.categoryId(), user)).thenReturn(category);
		when(articleSaveService.save(any(Article.class))).thenReturn(article);

		// when
		createArticleUsecase.createArticle(user, command);

		// then
		verify(articleGetService).checkExistsByUserAndUrl(user, command.url());
		verify(getCategoryPort).getCategoryAndUser(command.categoryId(), user);
		verify(articleSaveService).save(any(Article.class));
		verifyNoInteractions(getPushSubscription, scheduleArticleReminder);
	}
}
