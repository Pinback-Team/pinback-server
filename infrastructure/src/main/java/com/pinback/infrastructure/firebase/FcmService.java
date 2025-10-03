package com.pinback.infrastructure.firebase;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FcmService {
	public void sendNotification(String token, String url) {
		try {
			Message.Builder messageBuilder = Message.builder()
				.setToken(token)
				.setNotification(Notification.builder()
					.setTitle("pinback")
					.setBody("저장한 북마크를 확인해보세요")
					.setImage("https://pinback-image.s3.ap-northeast-2.amazonaws.com/FCM-IMG.png")
					.build())
				.putData("image", "https://pinback-image.s3.ap-northeast-2.amazonaws.com/FCM-IMG.png")
				.putData("url", url);

			FirebaseMessaging.getInstance().send(messageBuilder.build());

		} catch (FirebaseMessagingException e) {
			throw new RuntimeException("익스텐션 알림 전송 실패", e);
		}
	}
}
