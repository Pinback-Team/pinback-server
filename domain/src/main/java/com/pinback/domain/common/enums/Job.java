package com.pinback.domain.common.enums;

import com.pinback.domain.user.exception.JobNotFoundException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Job {
	PLAN("PLAN", "기획"),
	DESIGN("DESIGN", "디자인"),
	FRONTEND("FRONTEND", "프론트엔드"),
	BACKEND("BACKEND", "백엔드");

	private final String key;
	private final String value;

	public static Job from(String value) {
		for (Job job : Job.values()) {
			if (job.getValue().equals(value)) {
				return job;
			}
		}
		throw new JobNotFoundException();
	}
}
