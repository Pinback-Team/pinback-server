package com.pinback.application.google.dto.response;

import java.util.UUID;

public record GoogleLoginResponse(
	boolean isUser,
	UUID userId,
	String email,
	String accessToken
) {
	public static GoogleLoginResponse loggedIn(UUID userId, String email, String accessToken) {
		return new GoogleLoginResponse(true, userId, email, accessToken);
	}

	public static GoogleLoginResponse tempLogin(UUID userId, String email) {
		return new GoogleLoginResponse(false, userId, email, null);
	}
}
