package com.pinback.infrastructure.firebase;

import org.springframework.stereotype.Component;

import com.pinback.application.test.port.out.FcmServicePort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FcmServiceAdapter implements FcmServicePort {
	private final FcmService fcmService;

	@Override
	public void sendNotification(String token, String message) {
		fcmService.sendNotification(token, message);
	}
}
