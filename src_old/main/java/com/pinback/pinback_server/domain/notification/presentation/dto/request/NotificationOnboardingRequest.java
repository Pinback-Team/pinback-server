package com.pinback.pinback_server.domain.notification.presentation.dto.request;

import com.pinback.pinback_server.domain.auth.presentation.dto.request.KeyCommand;
import com.pinback.pinback_server.domain.auth.presentation.dto.request.NotificationInfoCommand;

public record NotificationOnboardingRequest(
	String endpoint,
	KeyRequest keyRequest
) {
	public NotificationInfoCommand toCommand() {
		return NotificationInfoCommand.from(
			endpoint,
			new KeyCommand(keyRequest().p256dh(), keyRequest().auth())
		);
	}
}
