package com.pinback.application.article.usecase.command;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.article.dto.command.ArticleUpdateCommand;
import com.pinback.application.article.port.in.UpdateArticlePort;
import com.pinback.application.article.service.ArticleGetServicePort;
import com.pinback.application.category.port.out.CategoryGetServicePort;
import com.pinback.application.common.exception.MemoLengthLimitException;
import com.pinback.application.notification.service.NotificationService;
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

	private final ArticleGetServicePort articleGetServicePort;
	private final CategoryGetServicePort categoryGetServicePort;
	private final NotificationService notificationService;

	@Override
	public void updateArticle(User user, long articleId, ArticleUpdateCommand command) {
		validateMemoLength(command.memo());

		Article article = articleGetServicePort.findByUserAndId(user, articleId);
		boolean remindTimeChanged = !article.getRemindAt().equals(command.remindTime());

		Category category = categoryGetServicePort.getCategoryAndUser(command.categoryId(), user);
		article.update(command.memo(), category, command.remindTime());

		handleReminderUpdate(article, user, command.remindTime(), remindTimeChanged, articleId);
	}

	private void validateMemoLength(String memo) {
		if (TextUtil.countGraphemeClusters(memo) >= MEMO_LIMIT_LENGTH) {
			throw new MemoLengthLimitException();
		}
	}

	private void handleReminderUpdate(Article article, User user, LocalDateTime remindTime,
		boolean remindTimeChanged, long articleId) {
		if (remindTimeChanged) {
			notificationService.cancelArticleReminder(articleId, user.getId());

			if (remindTime != null && !remindTime.isBefore(LocalDateTime.now())) {
				PushSubscription subscriptionInfo = notificationService.findPushSubscription(user);
				notificationService.scheduleArticleReminder(article, user, subscriptionInfo.getToken());
			}
		}
	}
}
