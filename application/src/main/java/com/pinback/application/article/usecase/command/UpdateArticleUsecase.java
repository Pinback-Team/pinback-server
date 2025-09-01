package com.pinback.application.article.usecase.command;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.article.dto.command.ArticleUpdateCommand;
import com.pinback.application.article.port.in.UpdateArticlePort;
import com.pinback.application.article.port.out.ArticleGetServicePort;
import com.pinback.application.category.port.in.GetCategoryPort;
import com.pinback.application.common.exception.MemoLengthLimitException;
import com.pinback.application.notification.port.in.GetPushSubscriptionPort;
import com.pinback.application.notification.port.in.ManageArticleReminderPort;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.notification.entity.PushSubscription;
import com.pinback.domain.user.entity.User;
import com.pinback.shared.util.TextUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateArticleUsecase implements UpdateArticlePort {

	private static final long MEMO_LIMIT_LENGTH = 500;

	private final ArticleGetServicePort articleGetService;
	private final GetCategoryPort getCategoryPort;
	private final ManageArticleReminderPort manageArticleReminderPort;

	private final GetPushSubscriptionPort getPushSubscription;

	@Override
	public void updateArticle(User user, long articleId, ArticleUpdateCommand command) {
		validateMemoLength(command.memo());

		Article article = articleGetService.findByUserAndId(user, articleId);
		boolean remindTimeChanged = !Objects.equals(article.getRemindAt(), command.remindTime());

		Category category = getCategoryPort.getCategoryAndUser(command.categoryId(), user);
		article.update(command.memo(), category, command.remindTime());

		handleReminderUpdate(article, user, command.now(), command.remindTime(), remindTimeChanged, articleId);
	}

	private void validateMemoLength(String memo) {
		if (TextUtil.countGraphemeClusters(memo) >= MEMO_LIMIT_LENGTH) {
			throw new MemoLengthLimitException();
		}
	}

	private void handleReminderUpdate(Article article, User user, LocalDateTime now, LocalDateTime remindTime,
		boolean remindTimeChanged, long articleId) {
		if (remindTimeChanged) {
			manageArticleReminderPort.cancelArticleReminder(articleId, user.getId());

			if (remindTime != null && !remindTime.isBefore(now)) {
				PushSubscription subscriptionInfo = getPushSubscription.findPushSubscription(user);
				manageArticleReminderPort.scheduleArticleReminder(article, user, subscriptionInfo.getToken());
			}
		}
	}
}
