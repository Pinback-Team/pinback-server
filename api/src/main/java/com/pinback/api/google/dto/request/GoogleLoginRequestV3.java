package com.pinback.api.google.dto.request;

import com.pinback.application.google.dto.GoogleLoginCommandV3;

import jakarta.validation.constraints.NotNull;

public record GoogleLoginRequestV3(
	@NotNull(message = "인가 코드(code)는 비어있을 수 없습니다.")
	String code,
	@NotNull(message = "구글 로그인 리다이렉션 uri는 비어있을 수 없습니다.")
	String uri
) {
	public GoogleLoginCommandV3 toCommand() {
		return new GoogleLoginCommandV3(code, uri);
	}
}
