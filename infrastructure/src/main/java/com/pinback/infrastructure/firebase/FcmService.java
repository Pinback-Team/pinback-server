package com.pinback.infrastructure.firebase;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FcmService {
	public void sendNotification(String token, String url) {
		if (token == null || token.isEmpty()) {
			log.error("FCM 전송 실패: 토큰이 유효하지 않음 (null or empty). URL={}", url);

			return;
		}
		try {
			Message.Builder messageBuilder = Message.builder()
				.setToken(token)
				.putData("title", "pinback")
				.putData("body", "저장한 북마크를 확인해보세요")
				.putData("image", "https://pinback-image.s3.ap-northeast-2.amazonaws.com/FCM-IMG.png")
				.putData("url", url);

			String messageId = FirebaseMessaging.getInstance().send(messageBuilder.build());
			log.info("FCM 메시지 전송 성공: Token={}, Message ID={}", token, messageId);

		} catch (FirebaseMessagingException e) {
			log.error("FCM API 호출 중 예외 발생: Token={}, Error Code={}, Error Message={}",
				token, e.getErrorCode(), e.getMessage(), e);
			throw new RuntimeException("익스텐션 알림 전송 실패", e);
		}
	}
}
