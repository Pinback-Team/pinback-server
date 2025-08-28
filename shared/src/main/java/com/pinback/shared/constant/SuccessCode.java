package com.pinback.shared.constant;

import lombok.Getter;

@Getter
public enum SuccessCode {

	OK("s2000", "요청이 성공했습니다."),
	NO_CONTENT("s2040", "요청이 성공했습니다."),
	CREATED("s2010", "요청이 성공했습니다.");

	private final String code;
	private final String message;

	SuccessCode(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
