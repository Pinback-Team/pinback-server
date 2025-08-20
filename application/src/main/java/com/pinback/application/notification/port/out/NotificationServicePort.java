package com.pinback.application.notification.port.out;

import java.util.UUID;

import com.pinback.domain.article.entity.Article;
import com.pinback.domain.user.entity.User;

public interface NotificationServicePort {

	void scheduleArticleReminder(Article article, User user, String fcmToken);

	void cancelArticleReminder(long articleId, UUID userId);
}
