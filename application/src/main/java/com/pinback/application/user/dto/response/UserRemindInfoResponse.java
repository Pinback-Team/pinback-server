package com.pinback.application.user.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record UserRemindInfoResponse(
	LocalDate remindDate,
	LocalTime remindTime
) {
	public static UserRemindInfoResponse of(LocalDate remindDate, LocalTime remindTime) {
		return new UserRemindInfoResponse(remindDate, remindTime);
	}
}
