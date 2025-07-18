package com.pinback.pinback_server.infra.redis.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.infra.redis.dto.NotificationData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisNotificationService {

	private static final String NOTIFICATION_PREFIX = "notification:";
	private static final String NOTIFICATION_PREFIX_DATA = "notification:data:";
	private final RedisTemplate<String, Object> objectRedisTemplate;

	public void scheduleArticleReminder(Article article, User user, String token) {

		try {
			String notificationId = generateNotificationId(article.getId(), user.getId());
			String notificationKey = NOTIFICATION_PREFIX + notificationId;
			String notificationDataKey = NOTIFICATION_PREFIX_DATA + notificationId;

			NotificationData notificationData = createNotificationData(article, user, token, article.getRemindAt());

			long ttlSeconds = calculateTTLSeconds(article.getRemindAt());

			objectRedisTemplate.opsForValue().set(notificationKey, notificationData, Duration.ofSeconds(ttlSeconds));
			objectRedisTemplate.opsForValue()
				.set(notificationDataKey, notificationData,
					Duration.ofSeconds(ttlSeconds + 100000)); //TODO: 명확한 TTL 정책 필요

			log.info("Article 알림 예약 완료: articleId={}, userId={}, notificationId={}, remindTime={}, ttlSeconds={}",
				article.getId(), user.getId(), notificationId, article.getRemindAt(), ttlSeconds);

		} catch (Exception e) {
			log.error("Article 알림 예약 실패: articleId={}, userId={}, remindTime={}",
				article.getId(), user.getId(), article.getRemindAt(), e);

		}
	}

	public void cancelArticleReminder(Long articleId, UUID userId) {
		try {
			String pattern = NOTIFICATION_PREFIX + articleId + ":" + userId + ":*";
			var keys = objectRedisTemplate.keys(pattern);

			if (keys != null && !keys.isEmpty()) {
				objectRedisTemplate.delete(keys);
				log.info("Article 알림 취소 완료: articleId={}, userId={}, 삭제된 키 수={}",
					articleId, userId, keys.size());
			} else {
				log.debug("취소할 알림이 없습니다: articleId={}, userId={}", articleId, userId);
			}

		} catch (Exception e) {
			log.error("Article 알림 취소 실패: articleId={}, userId={}", articleId, userId, e);
		}
	}

	private NotificationData createNotificationData(Article article, User user, String token, LocalDateTime remindAt) {
		return NotificationData.builder()
			.articleId(article.getId())
			.url(article.getUrl())
			.userId(user.getId().toString())
			.fcmToken(token)
			.scheduledTime(remindAt)
			.createdAt(LocalDateTime.now())
			.build();
	}

	private String generateNotificationId(Long articleId, UUID userId) {
		return articleId + ":" + userId + ":" + System.currentTimeMillis();
	}

	private long calculateTTLSeconds(LocalDateTime remindTime) {
		return Duration.between(LocalDateTime.now(), remindTime).getSeconds();
	}
}


