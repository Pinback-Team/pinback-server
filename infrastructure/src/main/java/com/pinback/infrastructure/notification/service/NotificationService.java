package com.pinback.infrastructure.notification.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.notification.port.out.NotificationServicePort;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.redis.service.RedisNotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService implements NotificationServicePort {

	private final RedisNotificationService redisNotificationService;

	@Override
	public void scheduleArticleReminder(Article article, User user, String fcmToken) {
		redisNotificationService.scheduleArticleReminder(article, user, fcmToken);

		log.info("Scheduling article reminder for user: {}, article: {}", user.getEmail(),
			article != null ? article.getId() : "null");
	}

	@Override
	public void cancelArticleReminder(long articleId, UUID userId) {
		redisNotificationService.cancelArticleReminder(articleId, userId);

		log.info("Cancelling article reminder for user: {}, article: {}", userId, articleId);
	}
}
