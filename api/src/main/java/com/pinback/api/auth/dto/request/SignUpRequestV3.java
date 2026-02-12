package com.pinback.api.auth.dto.request;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pinback.application.auth.dto.SignUpCommandV3;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignUpRequestV3(
	@NotBlank(message = "이메일은 비어있을 수 없습니다.")
	String email,

	@Schema(description = "기본 알림 시간", example = "08:30", pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm")
	@NotNull(message = "리마인드 시간은 비어있을 수 없습니다.")
	LocalTime remindDefault,

	@NotNull(message = "알림 정보는 비어있을 수 없습니다.")
	String fcmToken,

	@NotBlank(message = "직무는 비어있을 수 없습니다.")
	String job
) {
	public SignUpCommandV3 toCommand() {
		return new SignUpCommandV3(email, remindDefault, fcmToken, job);
	}
}
