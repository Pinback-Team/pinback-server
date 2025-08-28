package com.pinback.application.notification.port.in;

import java.util.UUID;

import com.pinback.domain.article.entity.Article;
import com.pinback.domain.user.entity.User;

public interface ManageArticleReminderPort {
	void cancelArticleReminder(long articleId, UUID userId);
	void scheduleArticleReminder(Article article, User user, String token);
}
