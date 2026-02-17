package com.pinback.application.constant.dto.response;

import java.util.List;

public record JobsResponse(
	List<JobResponse> jobs
) {
	public static JobsResponse of(List<JobResponse> jobs) {
		return new JobsResponse(jobs);
	}
}
