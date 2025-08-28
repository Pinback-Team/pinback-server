package com.pinback.application.article.port.out;

import java.util.UUID;

import com.pinback.domain.article.entity.Article;
import com.pinback.domain.notification.entity.PushSubscription;
import com.pinback.domain.user.entity.User;

public interface NotificationServicePort {
	PushSubscription findPushSubscription(User user);

	void scheduleArticleReminder(Article article, User user, String fcmToken);

	void cancelArticleReminder(long articleId, UUID userId);
}
