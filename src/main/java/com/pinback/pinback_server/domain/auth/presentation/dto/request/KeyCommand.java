package com.pinback.pinback_server.domain.auth.presentation.dto.request;

public record KeyCommand(
	String p256dh,
	String auth
) {
}
