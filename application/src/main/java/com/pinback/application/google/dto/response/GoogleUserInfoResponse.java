package com.pinback.application.google.dto.response;

public record GoogleUserInfoResponse(
	String email
) {
	public static GoogleUserInfoResponse from(String email) {
		return new GoogleUserInfoResponse(email);
	}
}
