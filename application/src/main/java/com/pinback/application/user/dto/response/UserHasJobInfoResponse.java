package com.pinback.application.user.dto.response;

public record UserHasJobInfoResponse(
	boolean hasJob
) {
	public static UserHasJobInfoResponse of(boolean hasJob) {
		return new UserHasJobInfoResponse(hasJob);
	}
}
