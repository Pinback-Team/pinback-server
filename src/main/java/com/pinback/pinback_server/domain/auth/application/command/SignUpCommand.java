package com.pinback.pinback_server.domain.auth.application.command;

import java.time.LocalTime;

import com.pinback.pinback_server.domain.auth.presentation.dto.request.NotificationInfoCommand;

public record SignUpCommand(
	String email,
	LocalTime remindDefault,
	NotificationInfoCommand notificationInfo
) {
	public static SignUpCommand of(String email, LocalTime remindDefault, NotificationInfoCommand notificationInfo) {
		return new SignUpCommand(email, remindDefault, notificationInfo);
	}
}
