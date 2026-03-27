package com.pinback.application.google.dto.response;

import java.util.UUID;

public record GoogleLoginResponseV3(
	boolean isUser,
	boolean hasJob,
	String jobRole,
	UUID userId,
	String email,
	String accessToken,
	String refreshToken
) {
	public static GoogleLoginResponseV3 loggedIn(boolean hasJob, String jobRole, UUID userId, String email,
		String accessToken,
		String refreshToken) {
		return new GoogleLoginResponseV3(true, hasJob, jobRole, userId, email, accessToken, refreshToken);
	}

	public static GoogleLoginResponseV3 tempLogin(UUID userId, String email) {
		return new GoogleLoginResponseV3(false, false, null, userId, email, null, null);
	}
}
