package com.pinback.pinback_server.domain.auth.presentation.dto.request;

public record NotificationInfoCommand(
	String endpoint,
	KeyCommand key
) {
	public static NotificationInfoCommand from(String endpoint, KeyCommand command) {
		return new NotificationInfoCommand(endpoint, command);
	}
}
