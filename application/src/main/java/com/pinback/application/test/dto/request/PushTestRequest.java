package com.pinback.application.test.dto.request;

public record PushTestRequest(
	String fcmToken,
	String message
) {
}
