package com.pinback.infrastructure.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.pinback.infrastructure.firebase.FcmService;
import com.pinback.infrastructure.redis.dto.NotificationData;

import lombok.extern.slf4j.Slf4j;

@Component
@Profile("!test")
@Slf4j
public class NotificationExpirationListener implements MessageListener {

	private static final String NOTIFICATION_PREFIX = "notification:";
	private static final String NOTIFICATION_PREFIX_DATA = "notification:data:";
	private final RedisTemplate<String, Object> objectRedisTemplate;
	private final FcmService fcmService;

	public NotificationExpirationListener(
		@Qualifier("objectRedisTemplate") RedisTemplate<String, Object> objectRedisTemplate,
		FcmService fcmService
	) {
		this.objectRedisTemplate = objectRedisTemplate;
		this.fcmService = fcmService;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String expiredKey = new String(message.getBody());

		if (!expiredKey.startsWith(NOTIFICATION_PREFIX)) {
			return;
		}

		try {
			String notificationId = expiredKey.substring(NOTIFICATION_PREFIX.length());
			String dataKey = NOTIFICATION_PREFIX_DATA + notificationId;
			NotificationData remainingData = (NotificationData)objectRedisTemplate.opsForValue().get(dataKey);

			if (remainingData == null) {
				log.warn("TTL 만료 이벤트 수신했으나 데이터 없음 (이미 삭제됨): notificationId={}", notificationId);
				return;
			}

			log.info("TTL 만료 알림 감지: notificationId={}, articleId={}, userId={}, " +
					"scheduledTime={}, url=\"{}\"",
				notificationId,
				remainingData.getArticleId(),
				remainingData.getUserId(),
				remainingData.getScheduledTime(),
				remainingData.getUrl());

			fcmService.sendNotification(remainingData.getFcmToken(), remainingData.getUrl());

			objectRedisTemplate.delete(dataKey);
			log.info("TTL 만료로 알림 자동 삭제: notificationId={}", notificationId);

		} catch (Exception e) {
			log.error("TTL 만료 이벤트 처리 중 오류 발생: expiredKey={}, error={}",
				expiredKey, e.getMessage(), e);
		}
	}
}
