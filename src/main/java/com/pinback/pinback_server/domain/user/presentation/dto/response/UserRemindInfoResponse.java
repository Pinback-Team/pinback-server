package com.pinback.pinback_server.domain.user.presentation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record UserRemindInfoResponse(
	LocalDate remindDate,
	LocalTime remindDateTime
) {
	public static UserRemindInfoResponse of(LocalDate remindDate, LocalTime remindDateTime) {
		return new UserRemindInfoResponse(remindDate, remindDateTime);
	}
}
