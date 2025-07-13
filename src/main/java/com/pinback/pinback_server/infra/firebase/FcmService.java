package com.pinback.pinback_server.infra.firebase;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FcmService {
	public void sendNotification(String token, String url) {
		try {
			Message.Builder messageBuilder = Message.builder()
				.setToken(token)
				.putData("url", url);

			FirebaseMessaging.getInstance().send(messageBuilder.build());

		} catch (FirebaseMessagingException e) {
			throw new RuntimeException("익스텐션 알림 전송 실패", e);
		}
	}
}

