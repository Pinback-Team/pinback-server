package com.pinback.pinback_server.domain.user.presentation.dto.response;

import java.time.LocalDateTime;

public record UserInfoResponse(
	int acornCount,
	LocalDateTime remindDateTime
) {
	public static UserInfoResponse of(int acornCount, LocalDateTime remindDateTime) {
		return new UserInfoResponse(acornCount, remindDateTime);
	}
}
