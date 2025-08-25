package com.pinback.pinback_server.domain.notification.presentation.dto.request;

public record KeyRequest(
	String p256dh,
	String auth
) {
}
