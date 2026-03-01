package com.pinback.application.auth.dto;

public record TokenResponse(
	String token
) {
	public static TokenResponse of(String token) {
		return new TokenResponse(token);
	}
}
