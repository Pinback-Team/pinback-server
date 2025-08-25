package com.pinback.pinback_server.domain.auth.presentation.dto.response;

public record SignUpResponse(
	String token
) {
	public static SignUpResponse from(String token) {
		return new SignUpResponse(token);
	}
}
