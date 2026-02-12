package com.pinback.application.google.dto.response;

import java.util.UUID;

public record GoogleLoginResponseV3(
	boolean isUser,
	boolean hasJob,
	UUID userId,
	String email,
	String accessToken
) {
	public static GoogleLoginResponseV3 loggedIn(boolean hasJob, UUID userId, String email, String accessToken) {
		return new GoogleLoginResponseV3(true, hasJob, userId, email, accessToken);
	}

	public static GoogleLoginResponseV3 tempLogin(UUID userId, String email) {
		return new GoogleLoginResponseV3(false, false, userId, email, null);
	}
}
