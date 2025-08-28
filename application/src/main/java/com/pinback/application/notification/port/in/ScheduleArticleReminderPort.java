package com.pinback.application.notification.port.in;

import java.util.UUID;

import com.pinback.domain.article.entity.Article;
import com.pinback.domain.user.entity.User;

public interface ScheduleArticleReminderPort {
	void schedule(Article article, User user, String fcmToken);

	void cancel(long articleId, UUID userId);
}
