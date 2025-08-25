package com.pinback.pinback_server.domain.user.presentation.dto.response;

import java.time.LocalDateTime;

public record UserInfoResponse(
	int acornCount,
	LocalDateTime nextRemind
) {
	public static UserInfoResponse of(int acornCount, LocalDateTime nextRemind) {
		return new UserInfoResponse(acornCount, nextRemind);
	}
}
