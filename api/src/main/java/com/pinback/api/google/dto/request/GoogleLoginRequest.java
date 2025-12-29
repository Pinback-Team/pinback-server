package com.pinback.api.google.dto.request;

import com.pinback.application.google.dto.GoogleLoginCommand;

import jakarta.validation.constraints.NotNull;

public record GoogleLoginRequest(
	@NotNull(message = "인가 코드(code)는 비어있을 수 없습니다.")
	String code
) {
	public GoogleLoginCommand toCommand() {
		return new GoogleLoginCommand(code);
	}
}
