package com.pinback.pinback_server.domain.test.application;

import org.springframework.stereotype.Service;

import com.pinback.pinback_server.domain.test.presentation.dto.request.PushTestRequest;
import com.pinback.pinback_server.infra.firebase.FcmService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestUsecase {
	private final FcmService fcmService;

	public void pushTest(PushTestRequest request) {
		fcmService.sendNotification(request.fcmToken(), request.message());
	}
}
