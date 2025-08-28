package com.pinback.application.user.dto.command;

import java.time.LocalTime;

public record CreateUserCommand(
	String email,
	LocalTime remindDefault
) {
}
