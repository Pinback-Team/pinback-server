package com.pinback.application.user.dto.response;

public record UserJobInfoResponse(
	String job
) {
	public static UserJobInfoResponse of(String job) {
		return new UserJobInfoResponse(job);
	}
}
