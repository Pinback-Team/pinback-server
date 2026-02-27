package com.pinback.application.auth.dto;

import java.time.LocalTime;

public record SignUpCommandV3(
	String email,
	LocalTime remindDefault,
	String fcmToken,
	String job
) {
}
