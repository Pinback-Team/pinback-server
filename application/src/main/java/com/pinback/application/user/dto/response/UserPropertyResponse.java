package com.pinback.application.user.dto.response;

import java.util.UUID;

public record UserPropertyResponse(
	UUID userId,
	String jobRole
) {
	public static UserPropertyResponse of(UUID id, String jobRole) {
		return new UserPropertyResponse(id, jobRole);
	}
}
