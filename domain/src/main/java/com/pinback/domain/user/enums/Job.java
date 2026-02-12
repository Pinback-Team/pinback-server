package com.pinback.domain.user.enums;

import com.pinback.domain.user.exception.JobNotFoundException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Job {
	PLAN("PLAN", "기획자"),
	DESIGN("DESIGN", "디자이너"),
	FRONTEND("FRONTEND", "프론트엔드 개발자"),
	BACKEND("BACKEND", "백엔드 개발자");

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
