package com.pinback.application.article.usecase.command;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.article.dto.command.ArticleCreateCommand;
import com.pinback.application.article.port.in.CreateArticlePort;
import com.pinback.application.article.service.ArticleGetService;
import com.pinback.application.article.service.ArticleSaveService;
import com.pinback.application.category.service.CategoryGetService;
import com.pinback.application.common.exception.ArticleAlreadyExistException;
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
public class CreateArticleUsecase implements CreateArticlePort {

	private static final long MEMO_LIMIT_LENGTH = 500;

	private final ArticleGetService articleGetService;
	private final ArticleSaveService articleSaveService;
	private final CategoryGetService categoryGetService;
	private final NotificationService notificationService;

	@Override
	public void createArticle(User user, ArticleCreateCommand command) {
		validateArticleCreation(user, command);

		Category category = categoryGetService.getCategoryAndUser(command.categoryId(), user);
		Article article = Article.create(command.url(), command.memo(), user, category, command.remindTime());
		Article savedArticle = articleSaveService.save(article);

		scheduleReminderIfNeeded(savedArticle, user, command.remindTime());
	}

	private void validateArticleCreation(User user, ArticleCreateCommand command) {
		if (articleGetService.checkExistsByUserAndUrl(user, command.url())) {
			throw new ArticleAlreadyExistException();
		}

		if (TextUtil.countGraphemeClusters(command.memo()) >= MEMO_LIMIT_LENGTH) {
			throw new MemoLengthLimitException();
		}
	}

	private void scheduleReminderIfNeeded(Article article, User user, LocalDateTime remindTime) {
		if (remindTime != null && !remindTime.isBefore(LocalDateTime.now())) {
			PushSubscription subscriptionInfo = notificationService.findPushSubscription(user);
			notificationService.scheduleArticleReminder(article, user, subscriptionInfo.getToken());
		}
	}
}
