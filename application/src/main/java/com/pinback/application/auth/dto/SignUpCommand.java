package com.pinback.application.auth.dto;

import java.time.LocalTime;

public record SignUpCommand(
	String email,
	LocalTime remindDefault,
	String fcmToken
) {
}
