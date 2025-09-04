package com.pinback.application.article.usecase.command;

import static com.pinback.application.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import com.pinback.application.ApplicationTestBase;
import com.pinback.application.article.dto.command.ArticleUpdateCommand;
import com.pinback.application.article.port.out.ArticleGetServicePort;
import com.pinback.application.category.port.in.GetCategoryPort;
import com.pinback.application.common.exception.MemoLengthLimitException;
import com.pinback.application.notification.port.in.GetPushSubscriptionPort;
import com.pinback.application.notification.port.in.ManageArticleReminderPort;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.notification.entity.PushSubscription;
import com.pinback.domain.user.entity.User;

class UpdateArticleUsecaseTest extends ApplicationTestBase {

	@Mock
	private ArticleGetServicePort articleGetService;
	@Mock
	private GetCategoryPort getCategoryPort;
	@Mock
	private ManageArticleReminderPort manageArticleReminderPort;
	@Mock
	private GetPushSubscriptionPort getPushSubscription;

	@InjectMocks
	private UpdateArticleUsecase updateArticleUsecase;

	private User user;
	private Category originalCategory;
	private Category newCategory;
	private Article article;
	private PushSubscription pushSubscription;

	@BeforeEach
	void setUp() {
		user = user();
		ReflectionTestUtils.setField(user, "id", java.util.UUID.randomUUID());
		originalCategory = categoryWithName(user, "원본 카테고리");
		ReflectionTestUtils.setField(originalCategory, "id", 1L);
		newCategory = categoryWithName(user, "새 카테고리");
		ReflectionTestUtils.setField(newCategory, "id", 2L);
		article = articleWithDate(user, "https://test.com", originalCategory, LocalDateTime.of(2025, 8, 20, 15, 0));
		ReflectionTestUtils.setField(article, "id", 1L);
		pushSubscription = pushSubscription(user);
	}

	@DisplayName("사용자는 아티클을 업데이트할 수 있다")
	@Test
	void updateArticle_Success() {
		// given
		Long articleId = 1L;
		LocalDateTime newRemindTime = LocalDateTime.of(2025, 9, 1, 14, 0);
		LocalDateTime now = LocalDateTime.of(2025, 9, 1, 13, 0);
		ArticleUpdateCommand command = new ArticleUpdateCommand(
			newCategory.getId(),
			"업데이트된 메모",
			now,
			newRemindTime
		);

		when(articleGetService.findByUserAndId(user, articleId)).thenReturn(article);
		when(getCategoryPort.getCategoryAndUser(newCategory.getId(), user)).thenReturn(newCategory);
		when(getPushSubscription.findPushSubscription(user)).thenReturn(pushSubscription);

		// when
		updateArticleUsecase.updateArticle(user, articleId, command);

		// then
		verify(articleGetService).findByUserAndId(user, articleId);
		verify(getCategoryPort).getCategoryAndUser(newCategory.getId(), user);
		verify(manageArticleReminderPort).cancelArticleReminder(articleId, user.getId());
		verify(getPushSubscription).findPushSubscription(user);
		verify(manageArticleReminderPort).scheduleArticleReminder(any(Article.class), eq(user), eq("testToken"));
	}

	@DisplayName("메모가 500자를 초과하면 업데이트할 수 없다")
	@Test
	void updateArticle_MemoTooLong_ThrowsException() {
		// given
		Long articleId = 1L;
		String longMemo = "a".repeat(501);
		LocalDateTime now = LocalDateTime.of(2025, 9, 1, 13, 0);
		ArticleUpdateCommand command = new ArticleUpdateCommand(
			originalCategory.getId(),
			longMemo,
			now,
			LocalDateTime.of(2025, 9, 1, 14, 0)
		);

		// when & then
		assertThatThrownBy(() -> updateArticleUsecase.updateArticle(user, articleId, command))
			.isInstanceOf(MemoLengthLimitException.class);

		verifyNoInteractions(articleGetService, getCategoryPort, manageArticleReminderPort, getPushSubscription);
	}

	@DisplayName("리마인더 시간이 변경되지 않으면 알림 스케줄링이 발생하지 않는다")
	@Test
	void updateArticle_SameRemindTime_NoScheduling() {
		// given
		Long articleId = 1L;
		LocalDateTime originalRemindTime = LocalDateTime.of(2025, 8, 20, 15, 0);
		LocalDateTime now = LocalDateTime.of(2025, 9, 1, 13, 0);
		ArticleUpdateCommand command = new ArticleUpdateCommand(
			originalCategory.getId(),
			"업데이트된 메모",
			now,
			originalRemindTime
		);

		when(articleGetService.findByUserAndId(user, articleId)).thenReturn(article);
		when(getCategoryPort.getCategoryAndUser(originalCategory.getId(), user)).thenReturn(originalCategory);

		// when
		updateArticleUsecase.updateArticle(user, articleId, command);

		// then
		verify(articleGetService).findByUserAndId(user, articleId);
		verify(getCategoryPort).getCategoryAndUser(originalCategory.getId(), user);
		verifyNoInteractions(manageArticleReminderPort, getPushSubscription);
	}

	@DisplayName("리마인더 시간을 null로 변경하면 기존 알림이 취소된다")
	@Test
	void updateArticle_RemindTimeToNull_CancelReminder() {
		// given
		Long articleId = 1L;
		LocalDateTime now = LocalDateTime.of(2025, 9, 1, 13, 0);
		ArticleUpdateCommand command = new ArticleUpdateCommand(
			originalCategory.getId(),
			"업데이트된 메모",
			now,
			null
		);

		when(articleGetService.findByUserAndId(user, articleId)).thenReturn(article);
		when(getCategoryPort.getCategoryAndUser(originalCategory.getId(), user)).thenReturn(originalCategory);

		// when
		updateArticleUsecase.updateArticle(user, articleId, command);

		// then
		verify(articleGetService).findByUserAndId(user, articleId);
		verify(getCategoryPort).getCategoryAndUser(originalCategory.getId(), user);
		verify(manageArticleReminderPort).cancelArticleReminder(articleId, user.getId());
		verify(manageArticleReminderPort, never()).scheduleArticleReminder(any(), any(), any());
		verifyNoInteractions(getPushSubscription);
	}

	@DisplayName("과거 시간으로 리마인더를 변경하면 알림이 예약되지 않는다")
	@Test
	void updateArticle_PastRemindTime_NoSchedule() {
		// given
		Long articleId = 1L;
		LocalDateTime pastRemindTime = LocalDateTime.of(2020, 1, 1, 12, 0);
		LocalDateTime now = LocalDateTime.of(2025, 9, 1, 13, 0);
		ArticleUpdateCommand command = new ArticleUpdateCommand(
			originalCategory.getId(),
			"업데이트된 메모",
			now,
			pastRemindTime
		);

		when(articleGetService.findByUserAndId(user, articleId)).thenReturn(article);
		when(getCategoryPort.getCategoryAndUser(originalCategory.getId(), user)).thenReturn(originalCategory);

		// when
		updateArticleUsecase.updateArticle(user, articleId, command);

		// then
		verify(articleGetService).findByUserAndId(user, articleId);
		verify(getCategoryPort).getCategoryAndUser(originalCategory.getId(), user);
		verify(manageArticleReminderPort).cancelArticleReminder(articleId, user.getId());
		verify(manageArticleReminderPort, never()).scheduleArticleReminder(any(), any(), any());
		verifyNoInteractions(getPushSubscription);
	}

	@DisplayName("카테고리 변경만 하고 리마인더가 null이면 알림 관련 작업은 수행하지 않는다")
	@Test
	void updateArticle_CategoryChangeOnly_NoNotification() {
		// given
		Long articleId = 1L;
		Article articleWithNullReminder = articleWithDate(user, "https://test.com", originalCategory, null);
		LocalDateTime now = LocalDateTime.of(2025, 9, 1, 13, 0);
		ArticleUpdateCommand command = new ArticleUpdateCommand(
			newCategory.getId(),
			"업데이트된 메모",
			now,
			null
		);

		when(articleGetService.findByUserAndId(user, articleId)).thenReturn(articleWithNullReminder);
		when(getCategoryPort.getCategoryAndUser(newCategory.getId(), user)).thenReturn(newCategory);

		// when
		updateArticleUsecase.updateArticle(user, articleId, command);

		// then
		verify(articleGetService).findByUserAndId(user, articleId);
		verify(getCategoryPort).getCategoryAndUser(newCategory.getId(), user);
		verifyNoInteractions(manageArticleReminderPort, getPushSubscription);
	}

	@DisplayName("미래 시간으로 리마인더를 새로 설정하면 알림이 예약된다")
	@Test
	void updateArticle_NewFutureRemindTime_ScheduleNotification() {
		// given
		Long articleId = 1L;
		Article articleWithNullReminder = articleWithDate(user, "https://test.com", originalCategory, null);
		LocalDateTime futureRemindTime = LocalDateTime.of(2025, 12, 31, 23, 59);
		LocalDateTime now = LocalDateTime.of(2025, 9, 1, 13, 0);
		ArticleUpdateCommand command = new ArticleUpdateCommand(
			originalCategory.getId(),
			"업데이트된 메모",
			now,
			futureRemindTime
		);

		when(articleGetService.findByUserAndId(user, articleId)).thenReturn(articleWithNullReminder);
		when(getCategoryPort.getCategoryAndUser(originalCategory.getId(), user)).thenReturn(originalCategory);
		when(getPushSubscription.findPushSubscription(user)).thenReturn(pushSubscription);

		// when
		updateArticleUsecase.updateArticle(user, articleId, command);

		// then
		verify(articleGetService).findByUserAndId(user, articleId);
		verify(getCategoryPort).getCategoryAndUser(originalCategory.getId(), user);
		verify(getPushSubscription).findPushSubscription(user);
		verify(manageArticleReminderPort).scheduleArticleReminder(any(Article.class), eq(user), eq("testToken"));
	}
}
