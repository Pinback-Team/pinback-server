package com.pinback.application.user.dto.command;

import java.time.LocalTime;
import java.util.UUID;

public record UpdateUserCommand(
	UUID userId,
	String email,
	LocalTime remindDefault
) {
}
