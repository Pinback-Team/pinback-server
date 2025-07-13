package com.pinback.pinback_server.domain.user.presentation.dto.response;

public record UserInfoResponse(
	int acornCount,
	String remindDateTime
) {
	public static UserInfoResponse of(int acornCount, String remindDateTime) {
		return new UserInfoResponse(acornCount, remindDateTime);
	}
}
