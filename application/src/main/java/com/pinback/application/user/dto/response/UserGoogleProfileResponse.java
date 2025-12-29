package com.pinback.application.user.dto.response;

public record UserGoogleProfileResponse(
	String googleProfile
) {
	public static UserGoogleProfileResponse of(String googleProfile) {
		return new UserGoogleProfileResponse(googleProfile);
	}
}
