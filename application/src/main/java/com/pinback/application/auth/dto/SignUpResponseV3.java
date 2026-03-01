package com.pinback.application.auth.dto;

public record SignUpResponseV3(
	String accessToken,
	String refreshToken
) {
	public static SignUpResponseV3 from(String accessToken, String refreshToken) {
		return new SignUpResponseV3(accessToken, refreshToken);
	}
}
