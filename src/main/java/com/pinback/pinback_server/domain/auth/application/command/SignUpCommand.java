package com.pinback.pinback_server.domain.auth.application.command;

import java.time.LocalTime;

public record SignUpCommand(
	String email,
	LocalTime remindDefault,
	String token
) {
	public static SignUpCommand of(String email, LocalTime remindDefault, String token) {
		return new SignUpCommand(email, remindDefault, token);
	}
}
