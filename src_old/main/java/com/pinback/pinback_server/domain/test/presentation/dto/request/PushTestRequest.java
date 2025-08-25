package com.pinback.pinback_server.domain.test.presentation.dto.request;

public record PushTestRequest(
	String fcmToken,
	String message
) {
}
