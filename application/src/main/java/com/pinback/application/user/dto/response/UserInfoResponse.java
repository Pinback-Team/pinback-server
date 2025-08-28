package com.pinback.application.user.dto.response;

import java.time.LocalDateTime;

public record UserInfoResponse(
	int acornCount,
	LocalDateTime nextRemind
) {
	public static UserInfoResponse of(int acornCount, LocalDateTime remindDateTime) {
		return new UserInfoResponse(acornCount, remindDateTime);
	}
}
